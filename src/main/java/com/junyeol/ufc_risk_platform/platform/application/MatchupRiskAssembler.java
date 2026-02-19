package com.junyeol.ufc_risk_platform.platform.application;

import com.junyeol.ufc_risk_platform.core.matchup.model.*;
import com.junyeol.ufc_risk_platform.core.matchup.service.*;
import com.junyeol.ufc_risk_platform.platform.api.dto.*;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class MatchupRiskAssembler {
    private final UncertaintyCalculator uncertaintyCalculator =
            new UncertaintyCalculator(UncertaintyPolicy.defaultPolicy());
    private final FinishVolatilityCalculator finishVolatilityCalculator =
            new FinishVolatilityCalculator();
    private final RecencyShiftDetector recencyShiftDetector =
            new RecencyShiftDetector();
    private final RiskScoreEngine riskScoreEngine =
            new RiskScoreEngine(RiskScorePolicy.defaultPolicy());

    /**
     * Day20에서는 "입력값 생성"을 임시로 두고,
     * 흐름(조합 구조)만 확정해도 된다.
     */
    public CoreRiskResult computeCoreRisk(long fightId) {

        //  (임시 입력) — Day21~에서 진짜 데이터로 교체
        double baseAdvantageRisk = 0.40;

        UncertaintyScore uncertainty = uncertaintyCalculator.calculate(
                new UncertaintyFactors(4, 1.0, 0.60)
        );

        FinishVolatilityScore finishVolatility =
                finishVolatilityCalculator.calculate(0.45, 0.40, 0.15);

        RecencyShiftScore recencyShift =
                recencyShiftDetector.detect(0.50, new double[]{0.70, 0.60, 0.55}, 0.20, 0.30);

        RiskScore riskScore = riskScoreEngine.calculate(
                baseAdvantageRisk,
                uncertainty.value(),
                finishVolatility.value(),
                recencyShift.value()
        );

        return new CoreRiskResult(riskScore, uncertainty, finishVolatility, recencyShift);
    }

    public RiskResponse toResponse(long fightId, CoreRiskResult r) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        String level = riskLevel(r.riskScore().value());

        return new RiskResponse(
                fightId,
                now.toString(),
                r.riskScore().value(),
                level,
                new RiskBreakdownResponse(
                        r.riskScore().breakdown().baseAdvantageRisk(),
                        r.riskScore().breakdown().uncertaintyRisk(),
                        r.riskScore().breakdown().finishVolatilityRisk(),
                        r.riskScore().breakdown().recencyShiftRisk()
                ),
                new UncertaintyResponse(
                        r.uncertainty().value(),
                        r.uncertainty().breakdown().dataSparsity(),
                        r.uncertainty().breakdown().statVariance(),
                        r.uncertainty().breakdown().recencyRisk()
                ),
                new FinishVolatilityResponse(
                        r.finishVolatility().value(),
                        new FinishDistributionResponse(
                                r.finishVolatility().distribution().ko(),
                                r.finishVolatility().distribution().sub(),
                                r.finishVolatility().distribution().dec()
                        ),
                        r.finishVolatility().breakdown().finishRate(),
                        r.finishVolatility().breakdown().methodMixEntropy(),
                        r.finishVolatility().breakdown().mismatchFactor()
                ),
                new RecencyShiftResponse(
                        r.recencyShift().value(),
                        r.recencyShift().breakdown().recentVsBaselineDelta(),
                        r.recencyShift().breakdown().trendSlope(),
                        r.recencyShift().breakdown().volatilitySpike()
                )
        );
    }

    private String riskLevel(double v) {
        if (v >= 0.75) return "HIGH";
        if (v >= 0.45) return "MEDIUM";
        return "LOW";
    }

    // core 결과 묶음 (platform.application 내부 전용)
    public record CoreRiskResult(
            RiskScore riskScore,
            UncertaintyScore uncertainty,
            FinishVolatilityScore finishVolatility,
            RecencyShiftScore recencyShift
    ) {}
}
