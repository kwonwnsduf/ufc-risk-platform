package com.junyeol.ufc_risk_platform.core.matchup.service;

import com.junyeol.ufc_risk_platform.core.matchup.model.StrikingAdvantageResult;
import com.junyeol.ufc_risk_platform.core.matchup.model.StyleStats;
import com.junyeol.ufc_risk_platform.core.matchup.port.FighterStyleProfilePort;
import com.junyeol.ufc_risk_platform.core.matchup.style.StrikingAdvantageCalculator;
import org.springframework.stereotype.Service;

@Service
public class StrikingAdvantageService {

    private final FighterStyleProfilePort fighterStyleProfilePort;
    private final StrikingAdvantageCalculator calculator = new StrikingAdvantageCalculator();

    public StrikingAdvantageService(FighterStyleProfilePort fighterStyleProfilePort) {
        this.fighterStyleProfilePort = fighterStyleProfilePort;
    }
    public StrikingAdvantageResult calculate(long fighterAId, long fighterBId) {
        StyleStats a = fighterStyleProfilePort.loadStyleStats(fighterAId);
        StyleStats b = fighterStyleProfilePort.loadStyleStats(fighterBId);
        return calculator.calculate(a, b);
    }
}
