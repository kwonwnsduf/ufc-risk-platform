package com.junyeol.ufc_risk_platform.platform.api.dto;

public record RiskBreakdownResponse(double baseAdvantageRisk,
                                    double uncertaintyRisk,
                                    double finishVolatilityRisk,
                                    double recencyShiftRisk) {
}
