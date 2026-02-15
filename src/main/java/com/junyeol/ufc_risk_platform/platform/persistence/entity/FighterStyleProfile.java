package com.junyeol.ufc_risk_platform.platform.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

@Entity
@Getter

public class FighterStyleProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "fighter_id", nullable = false)
    private Fighter fighter;

    @Column(nullable = false)
    private double slpm;  // strikes landed per minute


    @Column(nullable = false)
    private double strAcc; // strikes accuraccy

    @Column(nullable = false)
    private double strDef; // strikes defend

    @Column(nullable = false)
    private double tdAvg; //takedown average

    @Column(nullable = false)
    private double tdAcc;

    @Column(nullable = false)
    private double tdDef;

    @Column(nullable = false)
    private double subAvg;// submission attempts

    @Column(nullable = false)
    private Instant updatedAt;

    protected FighterStyleProfile() {}

    public FighterStyleProfile(
            Fighter fighter,
            double slpm,
            double strAcc,
            double strDef,
            double tdAvg,
            double tdAcc,
            double tdDef,
            double subAvg
    ) {
        this.fighter = fighter;
        this.slpm = slpm;
        this.strAcc = strAcc;
        this.strDef = strDef;
        this.tdAvg = tdAvg;
        this.tdAcc = tdAcc;
        this.tdDef = tdDef;
        this.subAvg = subAvg;
        this.updatedAt = Instant.now();
    }
    public void update(
            double slpm,
            double strAcc,
            double strDef,
            double tdAvg,
            double tdAcc,
            double tdDef,
            double subAvg
    ) {
        this.slpm = slpm;
        this.strAcc = strAcc;
        this.strDef = strDef;
        this.tdAvg = tdAvg;
        this.tdAcc = tdAcc;
        this.tdDef = tdDef;
        this.subAvg = subAvg;
        this.updatedAt = Instant.now();
    }

}
