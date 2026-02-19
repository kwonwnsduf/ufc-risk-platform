package com.junyeol.ufc_risk_platform.platform.api.dto;

public record FinishVolatilityResponse(double value,
                                       FinishDistributionResponse distribution,
                                       double finishRate,
                                       double methodMixEntropy,
                                       double mismatchFactor) {
}
