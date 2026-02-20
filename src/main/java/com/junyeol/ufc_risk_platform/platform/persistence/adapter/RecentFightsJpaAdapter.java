package com.junyeol.ufc_risk_platform.platform.persistence.adapter;

import com.junyeol.ufc_risk_platform.core.recency.RecentFightsPort;
import com.junyeol.ufc_risk_platform.platform.persistence.entity.Fight;
import com.junyeol.ufc_risk_platform.platform.persistence.repository.FightRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecentFightsJpaAdapter implements RecentFightsPort {
    private final FightRepository fightRepository;

    public RecentFightsJpaAdapter(FightRepository fightRepository) {
        this.fightRepository = fightRepository;
    }

    @Override
    public List<Fight> findRecentFightsOfFighter(Long fighterId, int limit) {
        return fightRepository.findRecentFightsOfFighter(fighterId, limit);
    }
}
