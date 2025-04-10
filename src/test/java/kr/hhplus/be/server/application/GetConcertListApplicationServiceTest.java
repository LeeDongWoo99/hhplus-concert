package kr.hhplus.be.server.application;

import kr.hhplus.be.server.domain.Concert;
import kr.hhplus.be.server.domain.ConcertRepository;
import kr.hhplus.be.server.domain.ConcertSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetConcertListApplicationServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private GetConcertListApplicationService getConcertListService;

    @Test
    @DisplayName("콘서트_목록을_정상적으로_조회한다")
    void getConcertList_success() {
        // given
        List<ConcertSession> sessions = new ArrayList<>();

        Concert concert1 = new Concert(UUID.randomUUID(),
                "아이유 콘서트",
                "잠실 체조 경기장",
                LocalDate.of(2025, 4, 1),
                LocalDate.of(2025, 4, 2),
                sessions
        );

        Concert concert2 = new Concert(
                UUID.randomUUID(),
                "BTS 콘서트",
                "고척돔",
                LocalDate.of(2025, 4, 3),
                LocalDate.of(2025, 4, 4),
                sessions
        );

        List<Concert> mockConcertList = Arrays.asList(concert1, concert2);
        when(concertRepository.findAll()).thenReturn(mockConcertList);

        // when
        List<Concert> result = getConcertListService.getConcertList();

        // then
        assertEquals(2, result.size());
        assertEquals("아이유 콘서트", result.get(0).name());
    }
}
