package com.junyeol.ufc_risk_platform.platform.persistence.repository;

import com.junyeol.ufc_risk_platform.core.matchup.model.WeightClass;
import com.junyeol.ufc_risk_platform.platform.persistence.entity.WeightClassBaseline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeightClassBaselineRepository extends JpaRepository<WeightClassBaseline, Long> {
    Optional<WeightClassBaseline> findByWeightClass(WeightClass weightClass);
}
