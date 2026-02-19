package com.junyeol.ufc_risk_platform.core.matchup.model;

public record RiskBreakdown( double baseAdvantageRisk,
                             double uncertaintyRisk,
                             double finishVolatilityRisk,
                             double recencyShiftRisk     ) {
    public RiskBreakdown {
        validate01(baseAdvantageRisk, "baseAdvantageRisk");
        validate01(uncertaintyRisk, "uncertaintyRisk");
        validate01(finishVolatilityRisk, "finishVolatilityRisk");
        validate01(recencyShiftRisk, "recencyShiftRisk");
    }

    private static void validate01(double v, String name) {
        if (v < 0.0 || v > 1.0) {
            throw new IllegalArgumentException(name + " must be between 0 and 1");
        }
    }
}
