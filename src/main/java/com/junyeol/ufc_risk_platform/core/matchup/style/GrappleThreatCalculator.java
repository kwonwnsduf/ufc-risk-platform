package com.junyeol.ufc_risk_platform.core.matchup.style;

import com.junyeol.ufc_risk_platform.core.matchup.model.GrappleThreatResult;
import com.junyeol.ufc_risk_platform.core.matchup.model.StyleStats;

public class GrappleThreatCalculator {
    public GrappleThreatResult calculate(StyleStats a, StyleStats b) {

        double aTdAcc = clamp01(a.tdAcc());
        double aTdDef = clamp01(a.tdDef());
        double bTdAcc = clamp01(b.tdAcc());
        double bTdDef = clamp01(b.tdDef());

        // 1) 그라운드 진입 기회(테이크다운 압력)
        double entryAtoB = (a.tdAvg() * aTdAcc) * (1.0 - bTdDef);
        double entryBtoA = (b.tdAvg() * bTdAcc) * (1.0 - aTdDef);

        // 2) 서브 위협 (subAvg가 기본, entry가 보정)
        double aToBThreat = a.subAvg() * (1.0 + entryAtoB / 2.0);
        double bToAThreat = b.subAvg() * (1.0 + entryBtoA / 2.0);

        double raw = aToBThreat - bToAThreat;

        // 정규화 (subAvg는 보통 작아서 tanh 스케일도 작게)
        double score = Math.tanh(raw / 1.5) * 100.0;

        return new GrappleThreatResult(score, aToBThreat, bToAThreat, raw);
    }

    private double clamp01(double v) {
        if (v < 0) return 0;
        if (v > 1) return 1;
        return v;
    }
}
