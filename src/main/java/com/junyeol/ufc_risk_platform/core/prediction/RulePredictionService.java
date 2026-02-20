package com.junyeol.ufc_risk_platform.core.prediction;

import java.util.Objects;

public final class RulePredictionService {
    private final ProbabilityAdjuster adjuster;

    public RulePredictionService(ProbabilityAdjuster adjuster) {
        this.adjuster = Objects.requireNonNull(adjuster);
    }

    public ProbabilityResult predict(MatchupContext ctx) {
        Objects.requireNonNull(ctx);

        ProbabilityAdjuster.AdjustedProbability ap = adjuster.adjust(ctx);

        // 안전장치: 합이 1인지 확인(부동소수 오차 보정)
        double red = ap.redProb();
        double blue = 1.0 - red;

        return new ProbabilityResult(
                red,
                blue,
                ap.confidence(),
                ap.reasons()
        );
    }
}
