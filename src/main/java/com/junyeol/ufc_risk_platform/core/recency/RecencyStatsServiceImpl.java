package com.junyeol.ufc_risk_platform.core.recency;

import com.junyeol.ufc_risk_platform.core.fight.model.FightResult;
import com.junyeol.ufc_risk_platform.platform.persistence.entity.Fight;
import com.junyeol.ufc_risk_platform.platform.persistence.entity.Fighter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecencyStatsServiceImpl implements RecencyStatsService{
    private final RecencyFightSelector selector;

    public RecencyStatsServiceImpl(RecencyFightSelector selector) {
        this.selector = selector;
    }

    @Override
    public RecencyAdjustedStats calculate(Long fighterId) {

        List<Fight> fights = selector.select(fighterId);
        RecencyWeightPolicy policy = selector.policy();

        double weightedWinSum = 0.0;

        for (int i = 0; i < fights.size(); i++) {
            Fight fight = fights.get(i);

            boolean isWin = isWinner(fight, fighterId);
            double weight = policy.weightForIndex(i);

            if (isWin) {
                weightedWinSum += weight;
            }
        }

        return new RecencyAdjustedStats(
                fights.size(),
                weightedWinSum
        );
    }

    private boolean isWinner(Fight fight, Long fighterId) {
        if (fight.getResult() == FightResult.DRAW) return false;


        Fighter red = fight.getRedCorner();
        Fighter blue = fight.getBlueCorner();

        return switch (fight.getResult()) {
            case RED_WIN -> red.getId().equals(fighterId);
            case BLUE_WIN -> blue.getId().equals(fighterId);
            default -> false;
        };
    }
}
