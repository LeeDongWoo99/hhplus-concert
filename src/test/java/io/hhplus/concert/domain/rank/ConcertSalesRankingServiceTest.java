package io.hhplus.concert.domain.rank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConcertSalesRankingServiceTest {

    @InjectMocks
    private ConcertSalesRankingService concertSalesRankingService;
    @Mock
    private RankRepository rankRepository;

    @Test
    void 판매량_증가_정상작동() {
        // given
        String concertId = "concertA";
        String expectedKey = "concert:sales:" + LocalDate.now();

        // when
        concertSalesRankingService.increaseConcertSales(concertId);

        // then
        verify(rankRepository, times(1)).incrementScore(expectedKey, concertId);
    }

    @Test
    void TOP5_콘서트_정상조회() {
        // given
        List<String> topConcerts = List.of("concertA", "concertB");
        when(rankRepository.findTopConcerts(anyString(), eq(5))).thenReturn(topConcerts);

        // when
        List<String> result = concertSalesRankingService.getTopSellingConcerts();

        // then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("concertA", result.get(0));
    }

    @Test
    void 콘서트_랭킹_정상조회() {
        // given
        String concertId = "concertB";
        when(rankRepository.getRank(anyString(), eq(concertId))).thenReturn(1);

        // when
        int rank = concertSalesRankingService.getRankOfConcert(concertId);

        // then
        Assertions.assertEquals(1, rank);
    }
}