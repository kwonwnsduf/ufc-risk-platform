package com.junyeol.ufc_risk_platform.platform.persistence.entity;

import com.junyeol.ufc_risk_platform.core.matchup.model.WeightClass;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

@Entity
@Table(name = "fight_histories",indexes={@Index(name="idx_fight_history_fight",columnList="fight_id")})
@Getter
public class FightHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Fight fight;

    private Integer redRecentWins;
    private Integer blueRecentWins;


    @Enumerated(EnumType.STRING)
    private WeightClass weightClassAtFight;

    private Instant snapshotAt;


    public FightHistory(Fight fight,
                        WeightClass weightClassAtFight,
                        Integer redRecentWins,
                        Integer blueRecentWins) {
        this.fight = fight;
        this.weightClassAtFight = weightClassAtFight;
        this.redRecentWins = redRecentWins;
        this.blueRecentWins = blueRecentWins;
        this.snapshotAt = Instant.now();
    }

}
