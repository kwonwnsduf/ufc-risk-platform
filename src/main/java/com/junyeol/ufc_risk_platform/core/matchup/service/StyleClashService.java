package com.junyeol.ufc_risk_platform.core.matchup.service;

import com.junyeol.ufc_risk_platform.core.matchup.model.StyleAxisScore;
import com.junyeol.ufc_risk_platform.core.matchup.model.StyleClashResult;
import com.junyeol.ufc_risk_platform.core.matchup.model.StyleMatchupMatrix;
import com.junyeol.ufc_risk_platform.core.matchup.model.StyleStats;
import com.junyeol.ufc_risk_platform.core.matchup.port.FighterStyleProfilePort;
import com.junyeol.ufc_risk_platform.core.matchup.style.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StyleClashService {
    private final FighterStyleProfilePort stylePort;
    private final StrikingAdvantageCalculator strikingCalc = new StrikingAdvantageCalculator();
    private final WrestlingAdvantageCalculator wrestlingCalc = new WrestlingAdvantageCalculator();
    private final GrappleThreatCalculator grappleCalc = new GrappleThreatCalculator();

    // Day14
    private final StyleMatchupMatrixBuilder matrixBuilder = new StyleMatchupMatrixBuilder();
    private final StyleClashCalculator clashCalc = new StyleClashCalculator();

    public StyleClashService(FighterStyleProfilePort stylePort) {
        this.stylePort = stylePort;
    }
    public StyleClashResult calculate(long fighterAId, long fighterBId) {
        StyleStats a = stylePort.loadStyleStats(fighterAId);
        StyleStats b = stylePort.loadStyleStats(fighterBId);

        double striking = strikingCalc.calculate(a, b).score();
        double wrestling = wrestlingCalc.calculate(a, b).score();
        double grapple  = grappleCalc.calculate(a, b).score();

        List<StyleAxisScore> axisScores = matrixBuilder.buildAxisScores(striking, wrestling, grapple);

        // 승리 경로 강도(간단 버전): A 우위 축의 최대 magnitude / B도 동일
        double aStrength = axisScores.stream()
                .filter(StyleAxisScore::aAdvantaged)
                .mapToDouble(StyleAxisScore::magnitude)
                .max().orElse(0.0);

        double bStrength = axisScores.stream()
                .filter(StyleAxisScore::bAdvantaged)
                .mapToDouble(StyleAxisScore::magnitude)
                .max().orElse(0.0);

        double clashScore = clashCalc.calculateClashScore(axisScores);

        StyleMatchupMatrix matrix = new StyleMatchupMatrix(
                fighterAId,
                fighterBId,
                axisScores,
                aStrength,
                bStrength
        );

        return new StyleClashResult(clashScore, aStrength, bStrength, matrix);
    }
}
