package com.junyeol.ufc_risk_platform.platform.persistence.repository;

import com.junyeol.ufc_risk_platform.platform.persistence.entity.Fighter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FighterRepository extends JpaRepository<Fighter, Long> {
    Optional<Fighter> findByName(String name);
}
