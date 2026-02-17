package com.junyeol.ufc_risk_platform.core.matchup.model;

public record StyleClashResult( double clashScore,            // 0~100 (충돌/변동성 정도)
                                double aWinPathStrength,      // 0~100
                                double bWinPathStrength,      // 0~100
                                StyleMatchupMatrix matrix ) {
}
