package com.junyeol.ufc_risk_platform.core.matchup.service;

public class UncertaintyPolicy {
    private final double wData;
    private final double wVariance;
    private final double wRecency;

    private final double varianceScale;


    public UncertaintyPolicy(double wData, double wVariance, double wRecency, double varianceScale) {
        double sum = wData + wVariance + wRecency;
        if (Math.abs(sum - 1.0) > 1e-9) throw new IllegalArgumentException("weights must sum to 1.0");
        if (varianceScale <= 0.0) throw new IllegalArgumentException("varianceScale must be > 0");

        this.wData = wData;
        this.wVariance = wVariance;
        this.wRecency = wRecency;
        this.varianceScale = varianceScale;
    }

    public static UncertaintyPolicy defaultPolicy() {
        // 기본값(예시): 데이터/분산에 더 비중, 최근성은 보조
        return new UncertaintyPolicy(0.4, 0.4, 0.2, 2.0);
    }

    public double wData() { return wData; }
    public double wVariance() { return wVariance; }
    public double wRecency() { return wRecency; }
    public double varianceScale() { return varianceScale; }
}
