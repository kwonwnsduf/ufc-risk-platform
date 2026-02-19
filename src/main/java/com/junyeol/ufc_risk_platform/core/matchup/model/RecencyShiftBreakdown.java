package com.junyeol.ufc_risk_platform.core.matchup.model;

public record RecencyShiftBreakdown(   double recentVsBaselineDelta,  // 최근 평균 - 기준 평균
                                       double trendSlope,             // 최근 경기들의 추세 기울기
                                       double volatilitySpike         // 최근 구간에서의 분산 증가
                                        ) {
    public RecencyShiftBreakdown {
        validate01(Math.abs(recentVsBaselineDelta), "recentVsBaselineDelta(abs)");
        validate01(trendSlope, "trendSlope");
        validate01(volatilitySpike, "volatilitySpike");
    }

    private static void validate01(double v, String name) {
        if (v < 0.0 || v > 1.0) {
            throw new IllegalArgumentException(name + " must be between 0 and 1");
        }
    }
}
