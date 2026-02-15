package com.junyeol.ufc_risk_platform.platform.persistence.entity;

import com.junyeol.ufc_risk_platform.core.fight.model.FightResult;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Table(name = "fights")
@Getter
public class Fight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Fighter redCorner;

    @ManyToOne(optional = false)
    private Fighter blueCorner;

    @Enumerated(EnumType.STRING)
    private FightResult result;

    private LocalDate fightDate;

    protected Fight() {}

    public Fight(Fighter red, Fighter blue, LocalDate fightDate) {
        this.redCorner = red;
        this.blueCorner = blue;
        this.fightDate = fightDate;
    }
}
