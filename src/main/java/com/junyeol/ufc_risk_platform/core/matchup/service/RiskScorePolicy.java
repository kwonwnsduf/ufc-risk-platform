package com.junyeol.ufc_risk_platform.core.matchup.service;

public class RiskScorePolicy {
    private final double wBase;
    private final double wUncertainty;
    private final double wFinishVolatility;
    private final double wRecencyShift;

    public RiskScorePolicy(
            double wBase,
            double wUncertainty,
            double wFinishVolatility,
            double wRecencyShift
    ) {
        double sum = wBase + wUncertainty + wFinishVolatility + wRecencyShift;
        if (Math.abs(sum - 1.0) > 1e-9) {
            throw new IllegalArgumentException("weights must sum to 1.0");
        }
        this.wBase = wBase;
        this.wUncertainty = wUncertainty;
        this.wFinishVolatility = wFinishVolatility;
        this.wRecencyShift = wRecencyShift;
    }
    public static RiskScorePolicy defaultPolicy() {

        return new RiskScorePolicy(
                0.35,
                0.25,
                0.25,
                0.15
        );
    }

    public double wBase() { return wBase; }
    public double wUncertainty() { return wUncertainty; }
    public double wFinishVolatility() { return wFinishVolatility; }
    public double wRecencyShift() { return wRecencyShift; }
}
