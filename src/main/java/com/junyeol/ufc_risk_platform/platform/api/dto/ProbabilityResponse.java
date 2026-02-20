package com.junyeol.ufc_risk_platform.platform.api.dto;

import java.util.List;

public record ProbabilityResponse(double redWinProb,
                                  double blueWinProb,
                                  String confidence,
                                  List<String> reasons) {
}
