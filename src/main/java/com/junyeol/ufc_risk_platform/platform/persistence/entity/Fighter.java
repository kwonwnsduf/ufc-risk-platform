package com.junyeol.ufc_risk_platform.platform.persistence.entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.junyeol.ufc_risk_platform.core.matchup.model.WeightClass;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name="fighters",indexes = {@Index(name="idx_fighter_name",columnList="name"),
@Index(name="idx_fighter_weight_class",columnList="weight_class")})
public class Fighter {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "weight_class", nullable = false, length = 30)
    private WeightClass weightClass;

    private Integer heightCm;
    private Integer reachCm;
    private Integer age;

    protected Fighter() {}

    public Fighter(String name,WeightClass weightClass){
        this.name = name;
        this.weightClass = weightClass;
    }





}
