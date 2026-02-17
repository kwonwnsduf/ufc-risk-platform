package com.junyeol.ufc_risk_platform.platform.persistence.adapter;

import com.junyeol.ufc_risk_platform.core.matchup.model.StyleStats;
import com.junyeol.ufc_risk_platform.core.matchup.port.FighterStyleProfilePort;
import com.junyeol.ufc_risk_platform.platform.persistence.entity.FighterStyleProfile;
import com.junyeol.ufc_risk_platform.platform.persistence.repository.FighterStyleProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FighterStyleProfileJpaAdapter implements FighterStyleProfilePort {
    private final FighterStyleProfileRepository repository;

    @Override
    public StyleStats loadStyleStats(long fighterId) {
        FighterStyleProfile e = repository.findByFighterId(fighterId)
                .orElseThrow(() -> new IllegalArgumentException("FighterStyleProfile not found. fighterId=" + fighterId));

        // ⚠️ e.getStrAcc()/getStrDef() 값이 0~1인지 반드시 프로젝트에서 통일할 것
        return new StyleStats(
                e.getSlpm(),
                e.getStrAcc(),
                e.getStrDef(),
                e.getSapm(),
                e.getTdAvg(),
                e.getTdAcc(),
                e.getTdDef(),
                e.getSubAvg()
        );
    }
}
