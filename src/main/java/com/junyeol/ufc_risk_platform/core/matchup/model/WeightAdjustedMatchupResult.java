package com.junyeol.ufc_risk_platform.core.matchup.model;

public record WeightAdjustedMatchupResult( WeightClass weightClass,
                                           WeightedStyleScores adjusted,
                                           StyleClashResult original) {
}
