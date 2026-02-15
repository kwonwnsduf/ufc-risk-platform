package com.junyeol.ufc_risk_platform.platform.persistence.repository;

import com.junyeol.ufc_risk_platform.platform.persistence.entity.Fight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FightRepository extends JpaRepository<Fight,Long> {
}
