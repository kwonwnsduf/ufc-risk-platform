package com.junyeol.ufc_risk_platform.core.recency;

import java.util.List;

public final class RecencyWeightPolicy {
    private final List<Double> weights;

    private RecencyWeightPolicy(List<Double> weights) {
        if (weights == null || weights.isEmpty()) {
            throw new IllegalArgumentException("weights must not be null/empty");
        }
        for (double w : weights) {
            if (w < 0.0 || w > 1.0) {
                throw new IllegalArgumentException("weight must be in [0,1]");
            }
        }
        this.weights = List.copyOf(weights);
    }

    public static RecencyWeightPolicy default5() {
        return new RecencyWeightPolicy(List.of(1.00, 0.85, 0.70, 0.55, 0.40));
    }

    public int maxFightCount() {
        return weights.size();
    }

    public double weightForIndex(int index) {
        if (index < 0) throw new IllegalArgumentException("index must be >= 0");
        return index < weights.size() ? weights.get(index) : 0.0;
    }

}
