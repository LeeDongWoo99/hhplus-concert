package io.hhplus.concert.domain.rank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@SpringBootTest
class ConcertSalesRankingIntegrationTest {

    @Autowired
    private ConcertSalesRankingService concertSalesRankingService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void setUp() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    void 판매량_증가_및_TOP5_정상조회() {
        // given
        String concert1 = "A";
        String concert2 = "B";

        // when
        concertSalesRankingService.increaseConcertSales(concert1);
        concertSalesRankingService.increaseConcertSales(concert2);
        concertSalesRankingService.increaseConcertSales(concert2);

        // then
        List<String> top = concertSalesRankingService.getTopSellingConcerts();

        Assertions.assertEquals(2, top.size());
        Assertions.assertEquals(concert2, top.get(0));
    }

    @Test
    void 여러_콘서트_판매량에_따른_랭킹_정상작동() {
        // given
        String concertA = "A"; // 판매량 30
        String concertB = "B"; // 판매량 20
        String concertC = "C"; // 판매량 40

        for (int i = 0; i < 30; i++) concertSalesRankingService.increaseConcertSales(concertA);
        for (int i = 0; i < 20; i++) concertSalesRankingService.increaseConcertSales(concertB);
        for (int i = 0; i < 40; i++) concertSalesRankingService.increaseConcertSales(concertC);

        // when
        int rankA = concertSalesRankingService.getRankOfConcert(concertA);
        int rankB = concertSalesRankingService.getRankOfConcert(concertB);
        int rankC = concertSalesRankingService.getRankOfConcert(concertC);

        // then
        Assertions.assertEquals(2, rankA);
        Assertions.assertEquals(3, rankB);
        Assertions.assertEquals(1, rankC);
    }
}
