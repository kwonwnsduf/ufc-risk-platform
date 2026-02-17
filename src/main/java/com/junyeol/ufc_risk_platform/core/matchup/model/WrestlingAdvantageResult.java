package com.junyeol.ufc_risk_platform.core.matchup.model;

public record WrestlingAdvantageResult( double score,          // -100 ~ +100 (A 우위면 +)
                                        double aToBTdPressure, // A가 B에게 성공시킬 기대 TD 압력
                                        double bToATdPressure, // B가 A에게 성공시킬 기대 TD 압력
                                        double rawDiff) {
}
