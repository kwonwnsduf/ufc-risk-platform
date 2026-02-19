package com.junyeol.ufc_risk_platform.platform.api;

import com.junyeol.ufc_risk_platform.platform.api.dto.RiskResponse;
import com.junyeol.ufc_risk_platform.platform.application.MatchupRiskQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matchups")
public class MatchupRiskController {
    private final MatchupRiskQueryService queryService;

    public MatchupRiskController(MatchupRiskQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping("/{id}/risk")
    public ResponseEntity<RiskResponse> getRisk(@PathVariable("id") long fightId) {
        RiskResponse response = queryService.getRisk(fightId);
        return ResponseEntity.ok(response);
    }
}
