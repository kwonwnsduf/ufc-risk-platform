package com.junyeol.ufc_risk_platform.core.recency;

import com.junyeol.ufc_risk_platform.platform.persistence.entity.Fight;

import java.util.List;

public interface RecentFightsPort {
    List<Fight> findRecentFightsOfFighter(Long fighterId, int limit);
}
