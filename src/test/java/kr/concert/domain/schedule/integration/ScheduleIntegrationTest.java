package kr.concert.domain.schedule.integration;


import kr.concert.TestContainerConfig;
import kr.concert.domain.concert.entity.Concert;
import kr.concert.domain.concert.repo.ConcertRepository;
import kr.concert.domain.schedule.entity.Schedule;
import kr.concert.domain.schedule.repo.ScheduleRepository;
import kr.concert.domain.schedule.service.ScheduleService;
import kr.concert.interfaces.reservation.ReservationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Testcontainers
@ActiveProfiles("test")
public class ScheduleIntegrationTest extends TestContainerConfig {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Test
    @DisplayName("해당 콘서트에 스케줄이 없으면 예외가 발생한다.")
    void noSchedulesThrowsException() {
        // given
        Concert concert = concertRepository.save(new Concert(null, "텅빈 콘서트"));

        // when & then
        assertThatThrownBy(() -> scheduleService.getSchedulesOfConcert(concert.getConcertId()))
                .isInstanceOf(ReservationException.ScheduleNotExistException.class)
                .hasMessage("Schedule Not Exists");
    }

    @Test
    @DisplayName("해당 콘서트에 스케줄이 존재하면 스케줄 리스트를 반환한다.")
    void existingSchedulesReturnList() {
        // given
        Concert concert = concertRepository.save(new Concert(null, "콜드플레이 콘서트"));
        Schedule savedSchedule = scheduleRepository.save(
                new Schedule(null, concert, LocalDateTime.of(2025, 5, 1, 19, 0))
        );

        // when
        List<Schedule> schedules = scheduleService.getSchedulesOfConcert(concert.getConcertId());

        // then
        assertThat(schedules).hasSize(1);
        assertThat(schedules.get(0).getScheduleId()).isEqualTo(savedSchedule.getScheduleId());
        assertThat(schedules.get(0).getConcert().getConcertId()).isEqualTo(concert.getConcertId());
    }
}
