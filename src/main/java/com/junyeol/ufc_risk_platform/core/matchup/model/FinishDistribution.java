package com.junyeol.ufc_risk_platform.core.matchup.model;

public record FinishDistribution(double ko,
                                 double sub,
                                 double dec) {
    public FinishDistribution {
        validate01(ko, "ko");
        validate01(sub, "sub");
        validate01(dec, "dec");

        double sum = ko + sub + dec;
        if (Math.abs(sum - 1.0) > 1e-9) {
            throw new IllegalArgumentException("ko+sub+dec must sum to 1.0");
        }
    }

    private static void validate01(double v, String name) {
        if (v < 0.0 || v > 1.0) throw new IllegalArgumentException(name + " must be between 0 and 1");
    }
}
