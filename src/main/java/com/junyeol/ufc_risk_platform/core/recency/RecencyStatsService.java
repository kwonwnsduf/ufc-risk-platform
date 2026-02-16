package com.junyeol.ufc_risk_platform.core.recency;

public interface RecencyStatsService {
    RecencyAdjustedStats calculate(Long fighterId);
}
