package com.junyeol.ufc_risk_platform.core.matchup.model;

public record FinishVolatilityScore( double value,
                                     FinishVolatilityBreakdown breakdown,
                                     FinishDistribution distribution) {
    public FinishVolatilityScore {
        if (value < 0.0 || value > 1.0) throw new IllegalArgumentException("value must be between 0 and 1");
        if (breakdown == null) throw new IllegalArgumentException("breakdown is required");
        if (distribution == null) throw new IllegalArgumentException("distribution is required");
    }
}
