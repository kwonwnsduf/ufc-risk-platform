package com.junyeol.ufc_risk_platform.core.matchup.model;

import java.util.EnumMap;
import java.util.Map;

public class WeightBaselinePolicy {
    private final Map<WeightClass, WeightBaseline> baselines = new EnumMap<>(WeightClass.class);
    public WeightBaselinePolicy() {
        // 예시값: 처음엔 “설명 가능한 규칙”으로 두고, 데이터 쌓이면 튜닝
        baselines.put(WeightClass.HEAVYWEIGHT,
                new WeightBaseline(WeightClass.HEAVYWEIGHT, 1.15, 0.85, 0.85, 1.20));
        baselines.put(WeightClass.LIGHT_HEAVYWEIGHT,
                new WeightBaseline(WeightClass.LIGHT_HEAVYWEIGHT, 1.10, 0.90, 0.90, 1.05));
        baselines.put(WeightClass.MIDDLEWEIGHT,
                new WeightBaseline(WeightClass.MIDDLEWEIGHT, 1.05, 1.10, 0.95, 1.08));

        baselines.put(WeightClass.LIGHTWEIGHT,
                new WeightBaseline(WeightClass.LIGHTWEIGHT, 1.00, 1.10, 1.05, 1.00));

        baselines.put(WeightClass.WELTERWEIGHT,
                new WeightBaseline(WeightClass.WELTERWEIGHT, 1.00, 1.10, 1.00, 1.00));
        baselines.put(WeightClass.FEATHERWEIGHT,
                new WeightBaseline(WeightClass.FEATHERWEIGHT, 0.95, 0.95, 1.05, 0.95));
        baselines.put(WeightClass.BANTAMWEIGHT,
                new WeightBaseline(WeightClass.BANTAMWEIGHT, 0.90, 0.90, 1.10, 0.90));
        baselines.put(WeightClass.FLYWEIGHT,
                new WeightBaseline(WeightClass.FLYWEIGHT, 0.85, 0.85, 1.15, 0.85));}
    public WeightBaseline get(WeightClass weightClass) {
        return baselines.getOrDefault(weightClass,
                new WeightBaseline(weightClass, 1.0, 1.0, 1.0, 1.0));
    }



}
