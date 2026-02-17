package com.junyeol.ufc_risk_platform.platform.persistence.adapter;

import com.junyeol.ufc_risk_platform.core.matchup.model.WeightClass;
import com.junyeol.ufc_risk_platform.core.matchup.port.FighterWeightClassPort;
import com.junyeol.ufc_risk_platform.platform.persistence.repository.FighterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FighterWeightClassJpaAdapter implements FighterWeightClassPort {
    private final FighterRepository fighterRepository;

    @Override
    public WeightClass loadWeightClass(long fighterId) {
        return fighterRepository.findById(fighterId)
                .orElseThrow(() -> new IllegalArgumentException("fighter not found: " + fighterId))
                .getWeightClass(); // Fighter 엔티티에 WeightClass 필드가 있다고 가정
    }
}
