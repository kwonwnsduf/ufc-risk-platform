package com.junyeol.ufc_risk_platform.core.prediction;

import java.util.ArrayList;
import java.util.List;
import static com.junyeol.ufc_risk_platform.core.prediction.PredictionReason.*;
public final class ProbabilityAdjuster {

    /**
     * 룰 파라미터(프로젝트에서 고정 상수로 시작)
     * - 오즈 엔진 들어가면 더 세밀하게 조정 가능
     */
    private final double styleImpact;      // styleScore가 확률에 미치는 크기
    private final double riskPull;         // risk가 50%로 당기는 강도
    private final double uncertaintyPull;  // uncertainty가 50%로 당기는 강도
    private final double volatilityClamp;  // volatility가 extreme 제한하는 정도
    private final double minProb;          // 안전 클램프 하한 (예: 0.35)
    private final double maxProb;          // 안전 클램프 상한 (예: 0.65)

    public ProbabilityAdjuster() {
        //  기본값 (너무 공격적이지 않게 “설명 가능 baseline”)
        this.styleImpact = 0.18;       // styleScore=+1이면 +18%p 이동 (0.50 -> 0.68)
        this.riskPull = 0.22;          // risk=1이면 50%로 22% 당김
        this.uncertaintyPull = 0.25;   // uncertainty=1이면 50%로 25% 당김
        this.volatilityClamp = 0.15;   // volatility=1이면 확률 범위를 더 좁힘
        this.minProb = 0.35;
        this.maxProb = 0.65;
    }

    public AdjustedProbability adjust(MatchupContext ctx) {
        List<PredictionReason> reasons = new ArrayList<>();

        // 1) baseline
        double p = 0.50;

        // 2) style advantage 반영
        // styleScore: +면 Red 유리, -면 Blue 유리
        double styleDelta = clamp(ctx.getStyleScore(), -1.0, 1.0) * styleImpact;
        p = p + styleDelta;

        if (styleDelta > 0) reasons.add(STYLE_ADVANTAGE_RED);
        else if (styleDelta < 0) reasons.add(STYLE_ADVANTAGE_BLUE);

        // 3) risk/uncertainty -> 50%로 회귀(감쇠)
        // p = p + (0.5 - p) * pullFactor
        double risk = clamp01(ctx.getRiskScore());
        double uncertainty = clamp01(ctx.getUncertaintyScore());

        if (risk >= 0.60) reasons.add(HIGH_RISK_PULL_TO_50);
        if (uncertainty >= 0.60) reasons.add(HIGH_UNCERTAINTY_PULL_TO_50);

        p = pullToHalf(p, risk * riskPull);
        p = pullToHalf(p, uncertainty * uncertaintyPull);

        // 4) finish volatility -> extreme 확률 제한 폭을 더 좁힘
        double vol = clamp01(ctx.getFinishVolatility());
        double extraClamp = vol * volatilityClamp; // volatility 클수록 clamp 폭을 좁힘

        if (vol >= 0.60) reasons.add(HIGH_VOLATILITY_LIMIT_EXTREMES);

        double dynamicMin = minProb + extraClamp; // volatility ↑ -> min ↑
        double dynamicMax = maxProb - extraClamp; // volatility ↑ -> max ↓
        if (dynamicMin > 0.49) dynamicMin = 0.49;
        if (dynamicMax < 0.51) dynamicMax = 0.51;

        double beforeClamp = p;
        p = clamp(p, dynamicMin, dynamicMax);
        if (beforeClamp != p) reasons.add(CLAMPED_TO_SAFE_RANGE);

        // 5) 정규화 (Blue = 1 - Red)
        double redProb = clamp01(p);
        double blueProb = 1.0 - redProb;

        // 6) confidence 산출 (Day21은 단순 규칙으로 충분)
        ConfidenceLevel confidence = deriveConfidence(ctx, reasons);

        // recency shift가 크면 confidence 낮추고 reason 남김
        if (ctx.getRecencyShift() >= 0.60) {
            reasons.add(RECENCY_SHIFT_REDUCE_CONFIDENCE);
            confidence = ConfidenceLevel.LOW;
        }

        return new AdjustedProbability(redProb, blueProb, confidence, reasons);
    }

    private ConfidenceLevel deriveConfidence(MatchupContext ctx, List<PredictionReason> reasons) {
        double u = clamp01(ctx.getUncertaintyScore());
        double r = clamp01(ctx.getRiskScore());
        double v = clamp01(ctx.getFinishVolatility());

        // 단순 규칙: 불확실/리스크/변동성 중 2개 이상 높으면 LOW
        int high = 0;
        if (u >= 0.60) high++;
        if (r >= 0.60) high++;
        if (v >= 0.60) high++;

        if (high >= 2) return ConfidenceLevel.LOW;
        if (high == 1) return ConfidenceLevel.MEDIUM;
        return ConfidenceLevel.HIGH;
    }

    private static double pullToHalf(double p, double factor) {
        factor = clamp(factor, 0.0, 1.0);
        return p + (0.50 - p) * factor;
    }

    private static double clamp01(double x) {
        return clamp(x, 0.0, 1.0);
    }

    private static double clamp(double x, double min, double max) {
        return Math.max(min, Math.min(max, x));
    }

    public record AdjustedProbability(
            double redProb,
            double blueProb,
            ConfidenceLevel confidence,
            List<PredictionReason> reasons
    ) {}
}
