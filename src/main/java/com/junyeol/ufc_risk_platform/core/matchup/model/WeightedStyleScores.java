package com.junyeol.ufc_risk_platform.core.matchup.model;

public record WeightedStyleScores( double strikingScoreAdj,
                                   double wrestlingScoreAdj,
                                   double grappleScoreAdj,
                                   double clashScoreAdj) {
}
