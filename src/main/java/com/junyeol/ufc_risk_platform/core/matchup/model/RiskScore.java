package com.junyeol.ufc_risk_platform.core.matchup.model;

public record RiskScore(double value,
                        RiskBreakdown breakdown) {
    public RiskScore {
        if (value < 0.0 || value > 1.0) {
            throw new IllegalArgumentException("riskScore must be between 0 and 1");
        }
        if (breakdown == null) {
            throw new IllegalArgumentException("breakdown is required");
        }
    }
}
