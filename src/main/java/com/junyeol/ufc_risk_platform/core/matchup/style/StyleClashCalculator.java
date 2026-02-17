package com.junyeol.ufc_risk_platform.core.matchup.style;

import com.junyeol.ufc_risk_platform.core.matchup.model.StyleAxisScore;

import java.util.List;

public class StyleClashCalculator {
    public double calculateClashScore(List<StyleAxisScore> axisScores) {

        double aTop = 0.0;
        double bTop = 0.0;
        int aCount = 0;
        int bCount = 0;

        for (StyleAxisScore s : axisScores) {
            double mag = s.magnitude();
            if (s.aAdvantaged()) {
                aCount++;
                aTop = Math.max(aTop, mag);
            } else if (s.bAdvantaged()) {
                bCount++;
                bTop = Math.max(bTop, mag);
            }
        }

        // 한쪽만 우위면 충돌은 낮다(경로가 한쪽으로 기울기 때문)
        if (aTop == 0.0 || bTop == 0.0) {
            return 0.0;
        }

        // 서로 강한 우위 축이 있을수록 충돌↑
        double baseClash = Math.sqrt(aTop * bTop); // 0~100

        // 우위 축이 분산될수록 변동성↑ (간단 보정)
        double diversity = 1.0 + 0.1 * Math.max(0, (aCount + bCount) - 2); // 1.0 ~ 1.2
        double clash = baseClash * diversity;

        return clamp0to100(clash);
    }

    private double clamp0to100(double v) {
        if (v < 0) return 0;
        if (v > 100) return 100;
        return v;
    }
}
