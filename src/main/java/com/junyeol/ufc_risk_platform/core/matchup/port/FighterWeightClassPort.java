package com.junyeol.ufc_risk_platform.core.matchup.port;

import com.junyeol.ufc_risk_platform.core.matchup.model.WeightClass;

public interface FighterWeightClassPort {
    WeightClass loadWeightClass(long fighterId);
}
