package com.junyeol.ufc_risk_platform.core.matchup.model;

public record FinishVolatilityBreakdown(double finishRate,          // KO+SUB 비중 (0~1)
                                        double methodMixEntropy,    // KO/SUB/DEC 혼합 정도 (0~1)
                                        double mismatchFactor       // 스타일 불일치(예: KO vs SUB)
                                         ) {
    public FinishVolatilityBreakdown {
        validate01(finishRate, "finishRate");
        validate01(methodMixEntropy, "methodMixEntropy");
        validate01(mismatchFactor, "mismatchFactor");
    }

    private static void validate01(double v, String name) {
        if (v < 0.0 || v > 1.0) throw new IllegalArgumentException(name + " must be between 0 and 1");
    }
}
