package com.junyeol.ufc_risk_platform.platform.persistence.repository;

import com.junyeol.ufc_risk_platform.platform.persistence.entity.Fight;
import com.junyeol.ufc_risk_platform.platform.persistence.entity.FightHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FightHistoryRepository extends JpaRepository<FightHistory,Long> {
    Optional<FightHistory> findTopByFightOrderBySnapshotAtDesc(Fight fight);
    Optional<FightHistory> findTopByFightIdOrderBySnapshotAtDesc(Long fightId);

}
