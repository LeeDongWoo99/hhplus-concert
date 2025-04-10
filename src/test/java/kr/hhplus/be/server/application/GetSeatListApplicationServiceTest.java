package kr.hhplus.be.server.application;

import kr.hhplus.be.server.domain.ConcertSession;
import kr.hhplus.be.server.domain.ConcertSessionRepository;
import kr.hhplus.be.server.domain.Seat;
import kr.hhplus.be.server.domain.SeatStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetSeatListApplicationServiceTest {

    @Mock
    private ConcertSessionRepository concertSessionRepository;

    @InjectMocks
    private GetSeatListApplicationService getSeatListApplicationService;

    @Test
    @DisplayName("콘서트 좌석 목록을 정상적으로 조회한다")
    void getSeatList() {
        // given
        UUID concertSeatId = UUID.randomUUID();
        UUID concertSessionId = UUID.randomUUID();
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.plusHours(2);
        int availableSeats = 50;

        List<Seat> expectedSeats = new ArrayList<>();
        expectedSeats.add(new Seat(concertSeatId, concertSessionId, 1L, "VIP", 10000, SeatStatus.AVAILABLE));
        expectedSeats.add(new Seat(concertSeatId, concertSessionId, 2L, "Standard", 5000, SeatStatus.RESERVED));

        ConcertSession concertSession = new ConcertSession(concertSeatId, concertSessionId, startDateTime, endDateTime, availableSeats, expectedSeats);

        when(concertSessionRepository.findById(concertSessionId)).thenReturn(Optional.of(concertSession));

        // when
        List<Seat> actualSeats = getSeatListApplicationService.getSeatList(concertSessionId);

        // then
        assertEquals(expectedSeats, actualSeats);
        assertEquals(SeatStatus.AVAILABLE, actualSeats.get(0).seatStatus());
        assertEquals(SeatStatus.RESERVED, actualSeats.get(1).seatStatus());
    }

    @Test
    @DisplayName("콘서트 세션을 찾을 수 없을 때 예외가 발생한다")
    void getSeatList_ThrowsException_WhenConcertSessionNotFound() {
        // given
        UUID concertSessionId = UUID.randomUUID();

        // when
        when(concertSessionRepository.findById(concertSessionId)).thenReturn(Optional.empty());

        // then
        assertThrows(RuntimeException.class, () -> getSeatListApplicationService.getSeatList(concertSessionId));
    }
}

