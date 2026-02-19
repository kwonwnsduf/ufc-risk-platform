package com.junyeol.ufc_risk_platform.platform.application;

import com.junyeol.ufc_risk_platform.platform.api.dto.RiskResponse;
import org.springframework.stereotype.Service;

@Service
public class MatchupRiskQueryService {
    private final MatchupRiskAssembler assembler;
    public MatchupRiskQueryService(MatchupRiskAssembler assembler) {
        this.assembler = assembler;
    }
    public RiskResponse getRisk(long matchupId) {

        var coreResult = assembler.computeCoreRisk(matchupId); // (임시) 내부에서 core 엔진 호출
        return assembler.toResponse(matchupId, coreResult);
    }
}
