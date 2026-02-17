package com.junyeol.ufc_risk_platform.core.matchup.model;

public record GrappleThreatResult(   double score,            // -100 ~ +100 (A 위협이 크면 +)
                                     double aToBThreat,       // A가 B에게 주는 서브 위협(기대치)
                                     double bToAThreat,       // B가 A에게 주는 서브 위협
                                     double rawDiff) {
}
