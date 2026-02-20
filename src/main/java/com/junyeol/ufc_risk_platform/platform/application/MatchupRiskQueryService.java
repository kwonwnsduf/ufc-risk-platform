package com.junyeol.ufc_risk_platform.platform.application;

import com.junyeol.ufc_risk_platform.platform.api.dto.RiskResponse;
import com.junyeol.ufc_risk_platform.platform.persistence.entity.Fight;
import com.junyeol.ufc_risk_platform.platform.persistence.repository.FightRepository;
import org.springframework.stereotype.Service;

@Service
public class MatchupRiskQueryService {
    private final MatchupRiskAssembler assembler;
    private final FightRepository fightRepository;

    public MatchupRiskQueryService(MatchupRiskAssembler assembler, FightRepository fightRepository) {
        this.assembler = assembler;
        this.fightRepository = fightRepository;
    }

    public RiskResponse getRisk(long matchupId) {
        Fight fight = fightRepository.findById(matchupId)
                .orElseThrow(() -> new IllegalArgumentException("Fight not found. id=" + matchupId));

        long redId = fight.getRedCorner().getId();
        long blueId = fight.getBlueCorner().getId();

        var coreResult = assembler.computeCoreRisk(redId, blueId);
        return assembler.toResponse(matchupId, coreResult);
    }
}
