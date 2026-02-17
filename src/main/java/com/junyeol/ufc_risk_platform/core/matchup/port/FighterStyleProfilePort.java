package com.junyeol.ufc_risk_platform.core.matchup.port;

import com.junyeol.ufc_risk_platform.core.matchup.model.StyleStats;

public interface FighterStyleProfilePort {
    StyleStats loadStyleStats(long fighterId);
}
