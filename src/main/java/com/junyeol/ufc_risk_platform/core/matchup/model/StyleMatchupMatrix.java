package com.junyeol.ufc_risk_platform.core.matchup.model;

import java.util.List;

public record StyleMatchupMatrix(long fighterAId,
                                 long fighterBId,
                                 List<StyleAxisScore> axisScores,  // 타격/레슬/그래플 축 점수
                                 double aWinPathStrength,          // A의 “승리 경로 강도” (0~100)
                                 double bWinPathStrength ) {
}
