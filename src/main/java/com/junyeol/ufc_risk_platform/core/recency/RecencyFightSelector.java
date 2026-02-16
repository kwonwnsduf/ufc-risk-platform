package com.junyeol.ufc_risk_platform.core.recency;

import com.junyeol.ufc_risk_platform.platform.persistence.entity.Fight;

import java.util.List;

public class RecencyFightSelector {
    private final RecentFightsPort recentFightsPort;

    private final RecencyWeightPolicy policy = RecencyWeightPolicy.default5();

    public RecencyFightSelector(RecentFightsPort recentFightsPort) {
        this.recentFightsPort = recentFightsPort;
    }

    public List<Fight> select(Long fighterId) {
        return recentFightsPort.findRecentFightsOfFighter(
                fighterId,
                policy.maxFightCount()
        );
    }
    public RecencyWeightPolicy policy() {
        return policy;
    }

}
