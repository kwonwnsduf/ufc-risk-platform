package com.junyeol.ufc_risk_platform.platform.persistence.entity;

import com.junyeol.ufc_risk_platform.core.matchup.model.WeightClass;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Table(name = "weight_class_baselines",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_weight_class", columnNames = {"weight_class"})
        })
public class WeightClassBaseline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "weight_class", nullable = false, length = 30)
    private WeightClass weightClass;

    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal avgFinishRate;

    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal avgDecisionRate;

    @Column(nullable = false)
    private Integer volatilityIndex;

    @Column(nullable = false)
    private Instant updatedAt;

    protected WeightClassBaseline() {}

    public WeightClassBaseline(WeightClass weightClass,
                               BigDecimal avgFinishRate,
                               BigDecimal avgDecisionRate,
                               Integer volatilityIndex) {
        this.weightClass = weightClass;
        this.avgFinishRate = avgFinishRate;
        this.avgDecisionRate = avgDecisionRate;
        this.volatilityIndex = volatilityIndex;
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }




}
