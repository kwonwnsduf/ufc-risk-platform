package com.junyeol.ufc_risk_platform.core.matchup.style;

import com.junyeol.ufc_risk_platform.core.matchup.model.StyleAxis;
import com.junyeol.ufc_risk_platform.core.matchup.model.StyleAxisScore;

import java.util.List;

public class StyleMatchupMatrixBuilder {

    public List<StyleAxisScore> buildAxisScores(
            double strikingScore,
            double wrestlingScore,
            double grappleScore
    ) {
        return List.of(
                new StyleAxisScore(StyleAxis.STRIKING, strikingScore),
                new StyleAxisScore(StyleAxis.WRESTLING, wrestlingScore),
                new StyleAxisScore(StyleAxis.GRAPPLE, grappleScore)
        );
    }
}
