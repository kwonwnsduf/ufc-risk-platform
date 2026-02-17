package com.junyeol.ufc_risk_platform.core.matchup.model;

public record StrikingAdvantageResult(    double score,        // -100 ~ +100 (A 우위면 +)
                                          double aToBPressure, // A가 B에게 넣는 유효 타격 압력
                                          double bToAPressure, // B가 A에게 넣는 유효 타격 압력
                                          double rawDiff       // 정규화 전 차이값(디버깅/설명용)
                                           ) {
}
