package com.junyeol.ufc_risk_platform.core.recency;

import lombok.Getter;

@Getter
public class RecencyAdjustedStats {
    private final int totalFights;
    private final double weightedWinScore;

    public RecencyAdjustedStats(int totalFights, double weightedWinScore) {
        this.totalFights = totalFights;
        this.weightedWinScore = weightedWinScore;
    }
}
