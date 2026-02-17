package com.junyeol.ufc_risk_platform.core.matchup.model;

public record StyleAxisScore(  StyleAxis axis,
                               double score) {
    public double magnitude() { // 우위 강도(0~100)
        return Math.min(100.0, Math.abs(score));
    }
    public boolean aAdvantaged() { return score > 0; }
    public boolean bAdvantaged() { return score < 0; }
}
