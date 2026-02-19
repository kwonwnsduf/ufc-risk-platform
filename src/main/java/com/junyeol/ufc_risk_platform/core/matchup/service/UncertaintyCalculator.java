package com.junyeol.ufc_risk_platform.core.matchup.service;

import com.junyeol.ufc_risk_platform.core.matchup.model.UncertaintyBreakdown;
import com.junyeol.ufc_risk_platform.core.matchup.model.UncertaintyFactors;
import com.junyeol.ufc_risk_platform.core.matchup.model.UncertaintyScore;

public class UncertaintyCalculator {

    private final UncertaintyPolicy policy;

    public UncertaintyCalculator(UncertaintyPolicy policy) {
        this.policy = (policy == null) ? UncertaintyPolicy.defaultPolicy() : policy;
    }

    public UncertaintyScore calculate(UncertaintyFactors f) {
        // 1) 데이터 부족 위험 (0~1)
        double dataSparsity = dataSparsityRisk(f.fightSampleSize());

        // 2) 분산 위험(기복) (0~1)
        double varianceRisk = varianceRisk(f.performanceVariance(), policy.varianceScale());

        // 3) 최근성 부족 위험 (0~1)
        // recencyCoverage(0~1)가 높을수록 신뢰도 높음 -> 위험은 낮아야 함
        double recencyRisk = clamp01(1.0 - f.recencyCoverage());

        // 4) 가중 합으로 최종 uncertainty(0~1)
        double uncertainty =
                policy.wData() * dataSparsity +
                        policy.wVariance() * varianceRisk +
                        policy.wRecency() * recencyRisk;

        // 5) breakdown 포함해서 반환
        return new UncertaintyScore(
                clamp01(uncertainty),
                new UncertaintyBreakdown(dataSparsity, varianceRisk, recencyRisk)
        );
    }

    // --- component 1: data sparsity ---
    private double dataSparsityRisk(int fights) {
        // 표본이 많을수록 위험↓ (너의 기준으로 조정 가능)
        if (fights >= 10) return 0.10;
        if (fights >= 7)  return 0.20;
        if (fights >= 5)  return 0.35;
        if (fights >= 3)  return 0.60;
        if (fights >= 1)  return 0.85;
        return 0.95; // 0경기 수준이면 거의 추정
    }

    // --- component 2: variance ---
    private double varianceRisk(double variance, double scale) {
        // variance를 scale로 나눠 0~1로 만들고, 1 초과는 클램프
        return clamp01(variance / scale);
    }

    private double clamp01(double v) {
        if (v < 0.0) return 0.0;
        if (v > 1.0) return 1.0;
        return v;
    }
}
