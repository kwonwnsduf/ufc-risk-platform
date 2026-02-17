package com.junyeol.ufc_risk_platform.core.matchup.model;

public record StyleStats(  double slpm,   // strikes landed per minute
                           double strAcc, // 0~1
                           double strDef, // 0~1
                           double sapm,// strikes absorbed per minute
                           double tdAvg,
                           double tdAcc,   // 0~1
                           double tdDef,
                           double subAvg
) {
}
