package com.junyeol.ufc_risk_platform.core.matchup.service;

import com.junyeol.ufc_risk_platform.core.matchup.model.RecencyShiftBreakdown;
import com.junyeol.ufc_risk_platform.core.matchup.model.RecencyShiftScore;

public class RecencyShiftDetector {
    /**
     * @param baselineAvg      장기 기준 평균 (0~1 정규화)
     * @param recentAverages   최근 N경기 평균 시퀀스 (최신 → 과거)
     * @param baselineVariance 장기 분산 (0~1 정규화)
     * @param recentVariance   최근 분산 (0~1 정규화)
     */
    public RecencyShiftScore detect(
            double baselineAvg,
            double[] recentAverages,
            double baselineVariance,
            double recentVariance
    ) {

        // 1) 최근 평균 vs 기준 평균 차이
        double recentAvg = mean(recentAverages);
        double delta = recentAvg - baselineAvg;
        double deltaRisk = clamp01(Math.abs(delta)); // 크기만 반영

        // 2) 최근 추세(기울기): 상승/하락이 빠를수록 위험
        double slope = trendSlope(recentAverages);
        double slopeRisk = clamp01(Math.abs(slope));

        // 3) 최근 변동성 스파이크
        double volatilitySpike =
                (baselineVariance <= 1e-9)
                        ? 0.0
                        : clamp01((recentVariance - baselineVariance) / baselineVariance);

        // 4) 가중합 (Day18 기본 철학)
        double shiftScore = clamp01(
                0.45 * deltaRisk +
                        0.35 * slopeRisk +
                        0.20 * volatilitySpike
        );

        return new RecencyShiftScore(
                shiftScore,
                new RecencyShiftBreakdown(deltaRisk, slopeRisk, volatilitySpike)
        );
    }

    // ---------- helpers ----------

    private double mean(double[] arr) {
        if (arr == null || arr.length == 0) return 0.0;
        double s = 0.0;
        for (double v : arr) s += v;
        return s / arr.length;
    }

    /**
     * 단순 선형 추세: (최신 - 과거) / (N-1)
     * 값이 클수록 최근 변화가 급격
     */
    private double trendSlope(double[] arr) {
        if (arr == null || arr.length < 2) return 0.0;
        double newest = arr[0];
        double oldest = arr[arr.length - 1];
        return (newest - oldest) / (arr.length - 1);
    }

    private double clamp01(double v) {
        if (v < 0.0) return 0.0;
        if (v > 1.0) return 1.0;
        return v;
    }
}
