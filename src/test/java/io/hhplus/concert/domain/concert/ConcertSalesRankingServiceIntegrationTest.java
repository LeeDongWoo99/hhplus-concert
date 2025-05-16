package io.hhplus.concert.domain.concert;

import io.hhplus.concert.TestcontainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class ConcertSalesRankingServiceIntegrationTest {

    @Autowired
    private ConcertSalesRankingService concertSalesRankingService;

    @Autowired
    private RedissonClient redissonClient;

    @BeforeEach
    void setUp() {
        RScoredSortedSet<String> set = redissonClient.getScoredSortedSet("concert:sales:");
        set.clear();
    }

    @Test
    void top5_콘서트를_조회한다() {
        // given
        RScoredSortedSet<String> set = redissonClient.getScoredSortedSet("concert:sales:");
        set.addScore("concert1", 5);
        set.addScore("concert2", 10);
        set.addScore("concert3", 3);
        set.addScore("concert4", 8);
        set.addScore("concert5", 1);

        // when
        List<String> topConcerts = concertSalesRankingService.getTopSellingConcerts();

        // then
        List<String> expectedOrder = List.of("concert2", "concert4", "concert1", "concert3", "concert5");
        assertEquals(5, topConcerts.size());
        assertIterableEquals(expectedOrder, new ArrayList<>(topConcerts));
    }

    @Test
    void 특정_콘서트의_순위를_확인한다() {
        // given
        RScoredSortedSet<String> set = redissonClient.getScoredSortedSet("concert:sales:");
        set.addScore("concertA", 100);
        set.addScore("concertB", 50);
        set.addScore("concertC", 25);

        // when
        Integer rankA = concertSalesRankingService.getRankOfConcert("concertA");
        Integer rankB = concertSalesRankingService.getRankOfConcert("concertB");
        Integer rankC = concertSalesRankingService.getRankOfConcert("concertC");

        // then
        assertEquals(1, rankA);
        assertEquals(2, rankB);
        assertEquals(3, rankC);
    }

    @Test
    void 랭킹정보가_없을때_예외를_발생시킨다() {
        // given: Redis에 아무 데이터도 없음

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            concertSalesRankingService.getTopSellingConcerts();
        });
        assertEquals("순위를 매길 수 있는 콘서트가 없습니다.", exception.getMessage());
    }
}
