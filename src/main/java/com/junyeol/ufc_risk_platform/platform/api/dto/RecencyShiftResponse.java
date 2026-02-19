package com.junyeol.ufc_risk_platform.platform.api.dto;

public record RecencyShiftResponse( double value,
                                    double recentVsBaselineDelta,
                                    double trendSlope,
                                    double volatilitySpike) {
}
