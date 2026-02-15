package com.junyeol.ufc_risk_platform.platform.persistence.repository;

import com.junyeol.ufc_risk_platform.platform.persistence.entity.FighterStyleProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FighterStyleProfileRepository extends JpaRepository<FighterStyleProfile,Long> {
    Optional<FighterStyleProfile> findByFighterId(Long fighterId);
}
