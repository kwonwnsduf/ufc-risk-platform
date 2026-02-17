package com.junyeol.ufc_risk_platform.core.matchup.style;

import com.junyeol.ufc_risk_platform.core.matchup.model.StrikingAdvantageResult;
import com.junyeol.ufc_risk_platform.core.matchup.model.StyleStats;

public class StrikingAdvantageCalculator {
    public StrikingAdvantageResult calculate(StyleStats a, StyleStats b){
        double aAcc = clamp01(a.strAcc());
        double aDef = clamp01(a.strDef());
        double bAcc = clamp01(b.strAcc());
        double bDef = clamp01(b.strDef());

        double aToB = (a.slpm() * aAcc) * (1.0 - bDef);
        double bToA = (b.slpm() * bAcc) * (1.0 - aDef);
        double raw = aToB - bToA;
        double score = Math.tanh(raw / 5.0) * 100.0;
        return new StrikingAdvantageResult(score, aToB, bToA, raw);
    }
    private double clamp01(double v) {
        if (v < 0) return 0;
        if (v > 1) return 1;
        return v;
    }
}
