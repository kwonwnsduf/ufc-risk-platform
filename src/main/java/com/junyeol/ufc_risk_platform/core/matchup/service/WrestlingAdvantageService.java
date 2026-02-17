package com.junyeol.ufc_risk_platform.core.matchup.service;

import com.junyeol.ufc_risk_platform.core.matchup.model.StyleStats;
import com.junyeol.ufc_risk_platform.core.matchup.model.WrestlingAdvantageResult;
import com.junyeol.ufc_risk_platform.core.matchup.port.FighterStyleProfilePort;
import com.junyeol.ufc_risk_platform.core.matchup.style.WrestlingAdvantageCalculator;
import org.springframework.stereotype.Service;

@Service
public class WrestlingAdvantageService {
    private final FighterStyleProfilePort fighterStyleProfilePort;
    private final WrestlingAdvantageCalculator calculator = new WrestlingAdvantageCalculator();

    public WrestlingAdvantageService(FighterStyleProfilePort fighterStyleProfilePort) {
        this.fighterStyleProfilePort = fighterStyleProfilePort;
    }
    public WrestlingAdvantageResult calculate(long fighterAId, long fighterBId) {
        StyleStats a = fighterStyleProfilePort.loadStyleStats(fighterAId);
        StyleStats b = fighterStyleProfilePort.loadStyleStats(fighterBId);
        return calculator.calculate(a, b);
    }
}
