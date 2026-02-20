package com.junyeol.ufc_risk_platform.core.prediction;

import java.util.List;

public final class ProbabilityResult {
    private final double redWinProb;   // 0~1
    private final double blueWinProb;  // 0~1
    private final ConfidenceLevel confidence;
    private final List<PredictionReason> reasons;

    public ProbabilityResult(double redWinProb,
                             double blueWinProb,
                             ConfidenceLevel confidence,
                             List<PredictionReason> reasons) {
        this.redWinProb = redWinProb;
        this.blueWinProb = blueWinProb;
        this.confidence = confidence;
        this.reasons = reasons;
    }

    public double getRedWinProb() { return redWinProb; }
    public double getBlueWinProb() { return blueWinProb; }
    public ConfidenceLevel getConfidence() { return confidence; }
    public List<PredictionReason> getReasons() { return reasons; }
}
