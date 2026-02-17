package com.junyeol.ufc_risk_platform.core.matchup.style;

import com.junyeol.ufc_risk_platform.core.matchup.model.StyleStats;
import com.junyeol.ufc_risk_platform.core.matchup.model.WrestlingAdvantageResult;

public class WrestlingAdvantageCalculator {
    public WrestlingAdvantageResult calculate(StyleStats a, StyleStats b) {

        double aAcc = clamp01(a.tdAcc());
        double aDef = clamp01(a.tdDef());
        double bAcc = clamp01(b.tdAcc());
        double bDef = clamp01(b.tdDef());

        // A -> B 테이크다운 압력 (기대 성공량)
        // TD Pressure = (TDavg * TDacc) * (1 - Opp.TDdef)
        double aToB = (a.tdAvg() * aAcc) * (1.0 - bDef);

        // B -> A 테이크다운 압력
        double bToA = (b.tdAvg() * bAcc) * (1.0 - aDef);

        double raw = aToB - bToA;

        // 정규화(스케일 값은 프로젝트 데이터 분포 보고 조절 가능)
        double score = Math.tanh(raw / 2.0) * 100.0;

        return new WrestlingAdvantageResult(score, aToB, bToA, raw);
    }
    private double clamp01(double v) {
        if (v < 0) return 0;
        if (v > 1) return 1;
        return v;
    }
}
