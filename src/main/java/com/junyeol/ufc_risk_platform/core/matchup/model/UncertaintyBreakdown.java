package com.junyeol.ufc_risk_platform.core.matchup.model;

public record UncertaintyBreakdown(        double dataSparsity,       // 표본 부족 위험(0~1)
                                           double statVariance,       // 기복/분산 위험(0~1)
                                           double recencyRisk         // 최근성 부족 위험(0~1)
                                            ) {
    public UncertaintyBreakdown {
        validate01(dataSparsity, "dataSparsity");
        validate01(statVariance, "statVariance");
        validate01(recencyRisk, "recencyRisk");
    }

    private static void validate01(double v, String name) {
        if (v < 0.0 || v > 1.0) throw new IllegalArgumentException(name + " must be between 0 and 1");
    }
}
