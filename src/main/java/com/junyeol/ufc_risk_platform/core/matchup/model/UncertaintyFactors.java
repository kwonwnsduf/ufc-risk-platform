package com.junyeol.ufc_risk_platform.core.matchup.model;

public record UncertaintyFactors(int fightSampleSize,       // 최근 N경기 표본 수(예: 3, 5, 10)
                                 double performanceVariance, // 퍼포먼스 분산(0 이상, 스케일은 너의 정의)
                                 double recencyCoverage      // 최근 경기 가중치 커버리지(0~1)
                                  ) {
    public UncertaintyFactors {
        if (fightSampleSize < 0) throw new IllegalArgumentException("fightSampleSize must be >= 0");
        if (performanceVariance < 0.0) throw new IllegalArgumentException("performanceVariance must be >= 0");
        if (recencyCoverage < 0.0 || recencyCoverage > 1.0)
            throw new IllegalArgumentException("recencyCoverage must be between 0 and 1");
    }
}
