package com.junyeol.ufc_risk_platform.core.matchup.service;

import com.junyeol.ufc_risk_platform.core.matchup.model.GrappleThreatResult;
import com.junyeol.ufc_risk_platform.core.matchup.model.StyleStats;
import com.junyeol.ufc_risk_platform.core.matchup.port.FighterStyleProfilePort;
import com.junyeol.ufc_risk_platform.core.matchup.style.GrappleThreatCalculator;
import org.springframework.stereotype.Service;

@Service
public class GrappleThreatService {
    private final FighterStyleProfilePort fighterStyleProfilePort;
    private final GrappleThreatCalculator calculator = new GrappleThreatCalculator();

    public GrappleThreatService(FighterStyleProfilePort fighterStyleProfilePort) {
        this.fighterStyleProfilePort = fighterStyleProfilePort;
    }
    public GrappleThreatResult calculate(long fighterAId, long fighterBId) {
        StyleStats a = fighterStyleProfilePort.loadStyleStats(fighterAId);
        StyleStats b = fighterStyleProfilePort.loadStyleStats(fighterBId);
        return calculator.calculate(a, b);
    }
}
