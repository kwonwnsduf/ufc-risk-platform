package com.junyeol.ufc_risk_platform.platform.application;

import com.junyeol.ufc_risk_platform.core.matchup.model.*;
import com.junyeol.ufc_risk_platform.core.matchup.service.*;
import com.junyeol.ufc_risk_platform.core.prediction.MatchupContext;
import com.junyeol.ufc_risk_platform.core.prediction.ProbabilityAdjuster;
import com.junyeol.ufc_risk_platform.core.prediction.ProbabilityResult;
import com.junyeol.ufc_risk_platform.core.prediction.RulePredictionService;
import com.junyeol.ufc_risk_platform.core.recency.RecencyAdjustedStats;
import com.junyeol.ufc_risk_platform.core.recency.RecencyStatsService;
import com.junyeol.ufc_risk_platform.platform.api.dto.*;
import com.junyeol.ufc_risk_platform.platform.persistence.entity.Fighter;
import com.junyeol.ufc_risk_platform.platform.persistence.entity.FighterStyleProfile;
import com.junyeol.ufc_risk_platform.platform.persistence.repository.FighterStyleProfileRepository;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class MatchupRiskAssembler {
    private final UncertaintyCalculator uncertaintyCalculator;
    private final FinishVolatilityCalculator finishVolatilityCalculator;
    private final RecencyShiftDetector recencyShiftDetector;
    private final RiskScoreEngine riskScoreEngine;
    private final RulePredictionService rulePredictionService;
    private final StyleClashService styleClashService;
    private final RecencyStatsService recencyStatsService;
    private final FighterStyleProfileRepository styleProfileRepository;

    public MatchupRiskAssembler(
            StyleClashService styleClashService,
            RecencyStatsService recencyStatsService,
            FighterStyleProfileRepository styleProfileRepository
    ) {
        this.uncertaintyCalculator = new UncertaintyCalculator(UncertaintyPolicy.defaultPolicy());
        this.finishVolatilityCalculator = new FinishVolatilityCalculator();
        this.recencyShiftDetector = new RecencyShiftDetector();
        this.riskScoreEngine = new RiskScoreEngine(RiskScorePolicy.defaultPolicy());
        this.rulePredictionService = new RulePredictionService(new ProbabilityAdjuster());
        this.styleClashService = styleClashService;
        this.recencyStatsService = recencyStatsService;
        this.styleProfileRepository = styleProfileRepository;
    }

    public CoreRiskResult computeCoreRisk(long redFighterId, long blueFighterId) {
        StyleClashResult styleClash = styleClashService.calculate(redFighterId, blueFighterId);
        double styleScore = (styleClash.aWinPathStrength() - styleClash.bWinPathStrength()) / 100.0;

        RecencyAdjustedStats redRecency = recencyStatsService.calculate(redFighterId);
        RecencyAdjustedStats blueRecency = recencyStatsService.calculate(blueFighterId);

        FighterStyleProfile redProfile = styleProfileRepository.findByFighterId(redFighterId)
                .orElse(null);
        FighterStyleProfile blueProfile = styleProfileRepository.findByFighterId(blueFighterId)
                .orElse(null);

        double baseAdvantageRisk = calculateBaseAdvantageRisk(redRecency, blueRecency);
        double dataSparsity = calculateDataSparsity(redProfile, blueProfile);
        double statVariance = calculateStatVariance(redProfile, blueProfile);
        double recencyRisk = calculateRecencyRisk(redRecency, blueRecency);

        double finishRateRed = redProfile != null ? (redProfile.getSlpm() / 10.0) : 0.5;
        double finishRateBlue = blueProfile != null ? (blueProfile.getSlpm() / 10.0) : 0.5;
        double methodMixEntropy = calculateMethodMixEntropy(redProfile, blueProfile);
        double mismatchFactor = styleClash.clashScore();

        UncertaintyScore uncertainty = uncertaintyCalculator.calculate(
                new UncertaintyFactors(4, statVariance, recencyRisk)
        );

        FinishVolatilityScore finishVolatility =
                finishVolatilityCalculator.calculate(finishRateRed, finishRateBlue, mismatchFactor);

        double recentVsBaselineDelta = Math.abs(redRecency.getWeightedWinScore() - blueRecency.getWeightedWinScore());
        double[] recentForm = {redRecency.getWeightedWinScore(), blueRecency.getWeightedWinScore()};
        double trendSlope = calculateTrendSlope(recentForm);
        double volatilitySpike = Math.abs(redRecency.getTotalFights() - blueRecency.getTotalFights()) / 10.0;

        RecencyShiftScore recencyShift =
                recencyShiftDetector.detect(0.50, recentForm, recentVsBaselineDelta, volatilitySpike);

        RiskScore riskScore = riskScoreEngine.calculate(
                baseAdvantageRisk,
                uncertainty.value(),
                finishVolatility.value(),
                recencyShift.value()
        );
        MatchupContext ctx = new MatchupContext(
                styleScore,
                riskScore.value(),
                uncertainty.value(),
                finishVolatility.value(),
                recencyShift.value()
        );

        ProbabilityResult prob = rulePredictionService.predict(ctx);

        return new CoreRiskResult(riskScore, uncertainty, finishVolatility, recencyShift, prob);
    }

    private double calculateBaseAdvantageRisk(RecencyAdjustedStats red, RecencyAdjustedStats blue) {
        double redWinRate = red.getWeightedWinScore();
        double blueWinRate = blue.getWeightedWinScore();
        return Math.abs(redWinRate - blueWinRate);
    }

    private double calculateDataSparsity(FighterStyleProfile red, FighterStyleProfile blue) {
        int totalFights = (red != null ? 1 : 0) + (blue != null ? 1 : 0);
        return Math.max(0, 1.0 - (totalFights / 2.0));
    }

    private double calculateStatVariance(FighterStyleProfile red, FighterStyleProfile blue) {
        return 0.3;
    }

    private double calculateRecencyRisk(RecencyAdjustedStats red, RecencyAdjustedStats blue) {
        int redCount = red.getTotalFights();
        int blueCount = blue.getTotalFights();
        return Math.abs(redCount - blueCount) / 10.0;
    }

    private double calculateTrendSlope(double[] recentForm) {
        if (recentForm.length < 2) return 0.0;
        return recentForm[0] - recentForm[1];
    }

    private double calculateMethodMixEntropy(FighterStyleProfile red, FighterStyleProfile blue) {
        return 0.4;
    }

    public RiskResponse toResponse(long fightId, CoreRiskResult r) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        String level = riskLevel(r.riskScore().value());
        ProbabilityResponse prob = new ProbabilityResponse(
                r.probability().getRedWinProb(),
                r.probability().getBlueWinProb(),
                r.probability().getConfidence().name(),
                r.probability().getReasons().stream().map(Enum::name).toList()
        );

        return new RiskResponse(
                fightId,
                now.toString(),
                r.riskScore().value(),
                level,
                new RiskBreakdownResponse(
                        r.riskScore().breakdown().baseAdvantageRisk(),
                        r.riskScore().breakdown().uncertaintyRisk(),
                        r.riskScore().breakdown().finishVolatilityRisk(),
                        r.riskScore().breakdown().recencyShiftRisk()
                ),
                new UncertaintyResponse(
                        r.uncertainty().value(),
                        r.uncertainty().breakdown().dataSparsity(),
                        r.uncertainty().breakdown().statVariance(),
                        r.uncertainty().breakdown().recencyRisk()
                ),
                new FinishVolatilityResponse(
                        r.finishVolatility().value(),
                        new FinishDistributionResponse(
                                r.finishVolatility().distribution().ko(),
                                r.finishVolatility().distribution().sub(),
                                r.finishVolatility().distribution().dec()
                        ),
                        r.finishVolatility().breakdown().finishRate(),
                        r.finishVolatility().breakdown().methodMixEntropy(),
                        r.finishVolatility().breakdown().mismatchFactor()
                ),
                new RecencyShiftResponse(
                        r.recencyShift().value(),
                        r.recencyShift().breakdown().recentVsBaselineDelta(),
                        r.recencyShift().breakdown().trendSlope(),
                        r.recencyShift().breakdown().volatilitySpike()
                ),
                prob
        );
    }

    private String riskLevel(double v) {
        if (v >= 0.75) return "HIGH";
        if (v >= 0.45) return "MEDIUM";
        return "LOW";
    }

    public record CoreRiskResult(
            RiskScore riskScore,
            UncertaintyScore uncertainty,
            FinishVolatilityScore finishVolatility,
            RecencyShiftScore recencyShift,
            ProbabilityResult probability
    ) {}
}
