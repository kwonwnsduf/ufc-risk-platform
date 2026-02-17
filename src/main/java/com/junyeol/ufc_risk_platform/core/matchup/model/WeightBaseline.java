package com.junyeol.ufc_risk_platform.core.matchup.model;

public record WeightBaseline( WeightClass weightClass,
                              double strikingWeight,
                              double wrestlingWeight,
                              double grappleWeight,
                              double clashWeight) {
}
