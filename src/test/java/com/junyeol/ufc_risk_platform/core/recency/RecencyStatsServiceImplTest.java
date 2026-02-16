package com.junyeol.ufc_risk_platform.core.recency;

import com.junyeol.ufc_risk_platform.core.fight.model.FightResult;
import com.junyeol.ufc_risk_platform.platform.persistence.entity.Fight;
import com.junyeol.ufc_risk_platform.platform.persistence.entity.Fighter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecencyStatsServiceImplTest {
    @Test
    void all_wins_should_sum_all_weights() {
        Long fighterId = 1L;

        Fighter me = fighter(fighterId);
        Fighter opp = fighter(2L);

        List<Fight> fights = List.of(
                fight(me, opp, LocalDate.of(2026, 2, 10), FightResult.RED_WIN),
                fight(me, opp, LocalDate.of(2026, 1, 10), FightResult.RED_WIN),
                fight(me, opp, LocalDate.of(2025, 12, 10), FightResult.RED_WIN),
                fight(me, opp, LocalDate.of(2025, 11, 10), FightResult.RED_WIN),
                fight(me, opp, LocalDate.of(2025, 10, 10), FightResult.RED_WIN)
        );

        RecentFightsPort fakePort = (id, limit) -> fights; // 이미 최신순이라고 가정
        RecencyFightSelector selector = new RecencyFightSelector(fakePort);
        RecencyStatsService service = new RecencyStatsServiceImpl(selector);

        RecencyAdjustedStats stats = service.calculate(fighterId);

        assertThat(stats.getTotalFights()).isEqualTo(5);
        assertThat(stats.getWeightedWinScore()).isEqualTo(1.00 + 0.85 + 0.70 + 0.55 + 0.40);
    }

    @Test
    void mixed_results_should_sum_only_win_weights() {
        Long fighterId = 1L;

        Fighter me = fighter(fighterId);
        Fighter opp = fighter(2L);

        List<Fight> fights = List.of(
                fight(me, opp, LocalDate.of(2026, 2, 10), FightResult.RED_WIN),  // +1.00
                fight(me, opp, LocalDate.of(2026, 1, 10), FightResult.BLUE_WIN), // +0
                fight(me, opp, LocalDate.of(2025, 12, 10), FightResult.RED_WIN), // +0.70
                fight(me, opp, LocalDate.of(2025, 11, 10), FightResult.DRAW),   // +0
                fight(me, opp, LocalDate.of(2025, 10, 10), FightResult.RED_WIN) // +0.40
        );

        RecentFightsPort fakePort = (id, limit) -> fights;
        RecencyFightSelector selector = new RecencyFightSelector(fakePort);
        RecencyStatsService service = new RecencyStatsServiceImpl(selector);

        RecencyAdjustedStats stats = service.calculate(fighterId);

        assertThat(stats.getWeightedWinScore()).isEqualTo(1.00 + 0.70 + 0.40);
    }

    // ===== helper =====

    private Fighter fighter(Long id) {
        // Fighter 엔티티 생성자가 protected면 mock(또는 reflection) 방식 필요할 수 있음.
        // 여기서는 "id만 비교"하기 위해 간단히 스텁 객체로 처리한다고 가정.
        Fighter f = mock(Fighter.class);
        when(f.getId()).thenReturn(id);
        return f;
    }

    private Fight fight(Fighter red, Fighter blue, LocalDate date, FightResult result) {
        Fight f = new Fight(red, blue, date);

        // Fight.result는 생성자에 없으니 테스트에서 setter가 없으면 mock으로 만드는 게 현실적.
        // 아래는 mock 사용 버전.
        Fight mockFight = mock(Fight.class);

        when(mockFight.getRedCorner()).thenReturn(red);
        when(mockFight.getBlueCorner()).thenReturn(blue);
        when(mockFight.getFightDate()).thenReturn(date);
       when(mockFight.getResult()).thenReturn(result);

        return mockFight;
    }
}
