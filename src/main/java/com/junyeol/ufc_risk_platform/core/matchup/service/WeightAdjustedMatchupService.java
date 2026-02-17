package com.junyeol.ufc_risk_platform.core.matchup.service;

import com.junyeol.ufc_risk_platform.core.matchup.model.*;
import com.junyeol.ufc_risk_platform.core.matchup.port.FighterWeightClassPort;

public class WeightAdjustedMatchupService {
    private final StyleClashService styleClashService;
    private final FighterWeightClassPort weightClassPort;
    private final WeightBaselinePolicy baselinePolicy = new WeightBaselinePolicy();

    public WeightAdjustedMatchupService(
            StyleClashService styleClashService,
            FighterWeightClassPort weightClassPort
    ) {
        this.styleClashService = styleClashService;
        this.weightClassPort = weightClassPort;
    }
    public WeightAdjustedMatchupResult calculate(long fighterAId, long fighterBId) {
        // 1) 체급 결정 (동일 체급이라는 가정; 다르면 예외 또는 catchweight 처리)
        WeightClass wc = weightClassPort.loadWeightClass(fighterAId);

        // 2) Day14 원본 결과
        StyleClashResult original = styleClashService.calculate(fighterAId, fighterBId);

        // 3) 체급 baseline 적용
        WeightBaseline b = baselinePolicy.get(wc);

        // Day14 matrix에서 축 점수를 꺼내기
        double striking = original.matrix().axisScores().stream()
                .filter(s -> s.axis() == StyleAxis.STRIKING)
                .findFirst().orElseThrow().score();

        double wrestling = original.matrix().axisScores().stream()
                .filter(s -> s.axis() == StyleAxis.WRESTLING)
                .findFirst().orElseThrow().score();

        double grapple = original.matrix().axisScores().stream()
                .filter(s -> s.axis() == StyleAxis.GRAPPLE)
                .findFirst().orElseThrow().score();

        double clash = original.clashScore(); // 0~100

        double strikingAdj = clampScore(striking * b.strikingWeight());
        double wrestlingAdj = clampScore(wrestling * b.wrestlingWeight());
        double grappleAdj  = clampScore(grapple  * b.grappleWeight());
        double clashAdj    = clamp0to100(clash   * b.clashWeight());

        WeightedStyleScores adjusted = new WeightedStyleScores(
                strikingAdj, wrestlingAdj, grappleAdj, clashAdj
        );

        return new WeightAdjustedMatchupResult(wc, adjusted, original);
    }

    private double clampScore(double v) {
        if (v < -100) return -100;
        if (v > 100) return 100;
        return v;
    }

    private double clamp0to100(double v) {
        if (v < 0) return 0;
        if (v > 100) return 100;
        return v;
    }
}
