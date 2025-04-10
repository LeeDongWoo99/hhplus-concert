package kr.hhplus.be.server.application;

import kr.hhplus.be.server.domain.Concert;
import kr.hhplus.be.server.domain.ConcertRepository;
import kr.hhplus.be.server.domain.ConcertSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetConcertSessionListApplicationServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private GetConcertSessionListApplicationService getConcertSessionListService;

    @Test
    @DisplayName("콘서트 세션 목록을 정상적으로 조회한다")
    void getConcertSessionList_success() {
        // given
        UUID concertId = UUID.randomUUID();

        List<ConcertSession> mockSessions = List.of(
                new ConcertSession(
                        UUID.randomUUID(),
                        concertId,
                        LocalDateTime.of(2025, 4, 20, 16, 0),
                        LocalDateTime.of(2025, 4, 20, 20, 0),
                        50
                ),
                new ConcertSession(
                        UUID.randomUUID(),
                        concertId,
                        LocalDateTime.of(2025, 4, 21, 16, 0),
                        LocalDateTime.of(2025, 4, 21, 20, 0),
                        50
                )
        );

        Concert mockConcert = new Concert(
                concertId,
                "Test Concert",
                "Location",
                LocalDate.of(2025,
                        4,
                        20),
                LocalDate.of(2025,
                        4,
                        21),
                mockSessions);
        when(concertRepository.findById(concertId)).thenReturn(Optional.of(mockConcert));

        // when
        List<ConcertSession> result = getConcertSessionListService.getConcertSessionList(concertId);

        // then
        assertEquals(2, result.size());
        assertEquals(50, result.get(0).availableSeats());
    }
}

