package com.junyeol.ufc_risk_platform.core.prediction;

import java.util.Objects;

public final class MatchupContext {
    private final double styleScore;        // -1 ~ +1 (권장). +면 Red 우세
    private final double riskScore;         // 0 ~ 1 (높을수록 변수 큼)
    private final double uncertaintyScore;  // 0 ~ 1 (높을수록 데이터 신뢰 낮음)
    private final double finishVolatility;  // 0 ~ 1 (높을수록 KO/SUB 변동 큼)
    private final double recencyShift;      // 0 ~ 1 (최근 급변 감지 강도) 없으면 0

    public MatchupContext(
            double styleScore,
            double riskScore,
            double uncertaintyScore,
            double finishVolatility,
            double recencyShift
    ) {
        this.styleScore = styleScore;
        this.riskScore = riskScore;
        this.uncertaintyScore = uncertaintyScore;
        this.finishVolatility = finishVolatility;
        this.recencyShift = recencyShift;
    }

    public double getStyleScore() { return styleScore; }
    public double getRiskScore() { return riskScore; }
    public double getUncertaintyScore() { return uncertaintyScore; }
    public double getFinishVolatility() { return finishVolatility; }
    public double getRecencyShift() { return recencyShift; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatchupContext that)) return false;
        return Double.compare(that.styleScore, styleScore) == 0
                && Double.compare(that.riskScore, riskScore) == 0
                && Double.compare(that.uncertaintyScore, uncertaintyScore) == 0
                && Double.compare(that.finishVolatility, finishVolatility) == 0
                && Double.compare(that.recencyShift, recencyShift) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(styleScore, riskScore, uncertaintyScore, finishVolatility, recencyShift);
    }
}
