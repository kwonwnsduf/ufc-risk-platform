package com.junyeol.ufc_risk_platform.core.matchup.service;

import com.junyeol.ufc_risk_platform.core.matchup.model.RiskBreakdown;
import com.junyeol.ufc_risk_platform.core.matchup.model.RiskScore;

public class RiskScoreEngine {
    private final RiskScorePolicy policy;

    public RiskScoreEngine(RiskScorePolicy policy) {
        this.policy = (policy == null)
                ? RiskScorePolicy.defaultPolicy()
                : policy;
    }

    /**
     * @param baseAdvantageRisk
     * @param uncertaintyRisk
     * @param finishVolatilityRisk
     * @param recencyShiftRisk
     */
    public RiskScore calculate(
            double baseAdvantageRisk,
            double uncertaintyRisk,
            double finishVolatilityRisk,
            double recencyShiftRisk
    ) {

        double risk =
                policy.wBase() * clamp01(baseAdvantageRisk) +
                        policy.wUncertainty() * clamp01(uncertaintyRisk) +
                        policy.wFinishVolatility() * clamp01(finishVolatilityRisk) +
                        policy.wRecencyShift() * clamp01(recencyShiftRisk);

        return new RiskScore(
                clamp01(risk),
                new RiskBreakdown(
                        clamp01(baseAdvantageRisk),
                        clamp01(uncertaintyRisk),
                        clamp01(finishVolatilityRisk),
                        clamp01(recencyShiftRisk)
                )
        );
    }

    private double clamp01(double v) {
        if (v < 0.0) return 0.0;
        if (v > 1.0) return 1.0;
        return v;
    }
}
