package com.junyeol.ufc_risk_platform.core.matchup.service;

import com.junyeol.ufc_risk_platform.core.matchup.model.FinishDistribution;
import com.junyeol.ufc_risk_platform.core.matchup.model.FinishVolatilityBreakdown;
import com.junyeol.ufc_risk_platform.core.matchup.model.FinishVolatilityScore;

public class FinishVolatilityCalculator {
    /**
     * @param koThreat        0~1 (KO로 끝날 위협)
     * @param subThreat       0~1 (SUB로 끝날 위협)
     * @param decLikelihood   0~1 (판정으로 갈 가능성)
     */
    public FinishVolatilityScore calculate(double koThreat, double subThreat, double decLikelihood) {

        double ko = clamp01(koThreat);
        double sub = clamp01(subThreat);
        double dec = clamp01(decLikelihood);

        // 1) 분포 만들기: 3개를 합이 1이 되도록 정규화
        FinishDistribution dist = normalizeToDistribution(ko, sub, dec);

        // 2) finishRate: KO+SUB 비중이 클수록 “터질 가능성” ↑
        double finishRate = clamp01(dist.ko() + dist.sub());

        // 3) methodMixEntropy: KO/SUB/DEC가 고르게 섞일수록 “결말 방식이 예측 불가”
        double entropy = normalizedEntropy(dist);

        // 4) mismatchFactor: KO와 SUB가 둘 다 높으면(양쪽 위협) 변동성 ↑
        double mismatch = clamp01(2.0 * Math.min(dist.ko(), dist.sub())); // 둘 다 높을 때만 커짐

        // 5) 최종 volatility = 가중합 (Day17 기본값)
        double volatility = clamp01(
                0.45 * finishRate +
                        0.35 * entropy +
                        0.20 * mismatch
        );

        return new FinishVolatilityScore(
                volatility,
                new FinishVolatilityBreakdown(finishRate, entropy, mismatch),
                dist
        );
    }

    private FinishDistribution normalizeToDistribution(double ko, double sub, double dec) {
        double sum = ko + sub + dec;
        if (sum <= 1e-12) {
            // 정보가 없으면 보수적으로 DEC 쏠림(혹은 1/3 균등도 가능)
            return new FinishDistribution(0.10, 0.10, 0.80);
        }
        return new FinishDistribution(ko / sum, sub / sum, dec / sum);
    }

    /**
     * 0~1로 정규화된 엔트로피:
     * - 0에 가까움: 한 방식에 몰림(예측 쉬움)
     * - 1에 가까움: 고르게 섞임(예측 어려움)
     */
    private double normalizedEntropy(FinishDistribution d) {
        double h = entropy(d.ko()) + entropy(d.sub()) + entropy(d.dec());
        double max = Math.log(3.0); // 3분포의 최대 엔트로피
        return clamp01(h / max);
    }

    private double entropy(double p) {
        if (p <= 1e-12) return 0.0;
        return -p * Math.log(p);
    }

    private double clamp01(double v) {
        if (v < 0.0) return 0.0;
        if (v > 1.0) return 1.0;
        return v;
    }
}
