package com.junyeol.ufc_risk_platform.platform.api.dto;

public record UncertaintyResponse(  double value,
                                    double dataSparsity,
                                    double statVariance,
                                    double recencyRisk) {
}
