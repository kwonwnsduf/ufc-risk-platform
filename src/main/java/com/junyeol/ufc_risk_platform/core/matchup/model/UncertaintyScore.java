package com.junyeol.ufc_risk_platform.core.matchup.model;

public record UncertaintyScore(     double value,
                                    UncertaintyBreakdown breakdown) {
    public UncertaintyScore {
        if (breakdown == null) throw new IllegalArgumentException("breakdown is required");
        if (value < 0.0 || value > 1.0) throw new IllegalArgumentException("uncertainty must be between 0 and 1");
    }
}
