package com.junyeol.ufc_risk_platform.platform.persistence.repository;

import com.junyeol.ufc_risk_platform.platform.persistence.entity.Fight;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FightRepository extends JpaRepository<Fight,Long> {
    @Query("""
        select f
        from Fight f
        where f.redCorner.id = :fighterId
           or f.blueCorner.id = :fighterId
        order by f.fightDate desc
    """)
    List<Fight> findRecentFightsOfFighter(
            @Param("fighterId") Long fighterId,
            Pageable pageable
    );
    default List<Fight> findRecentFightsOfFighter(Long fighterId, int limit) {
        return findRecentFightsOfFighter(
                fighterId,
                PageRequest.of(0, limit)
        );
    }

}
