package com.junyeol.ufc_risk_platform.platform.api.dto;

public record RiskResponse(  long matchupId,
                             String generatedAt,
                             double riskScore,
                             String riskLevel,
                             RiskBreakdownResponse breakdown,
                             UncertaintyResponse uncertainty,
                             FinishVolatilityResponse finishVolatility,
                             RecencyShiftResponse recencyShift) {
}
