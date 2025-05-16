package io.hhplus.concert.application.usecase.concert;

import io.hhplus.concert.domain.concert.ConcertSalesRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertUsecase {

    private final ConcertSalesRankingService concertSalesRankingService;

    /**
     * 인기 콘서트 TOP5 목록 조회
     */
    public ConcertResult.TopSellingConcerts getTop5Concerts() {
        List<String> topConcerts = concertSalesRankingService.getTopSellingConcerts();
        return ConcertResult.TopSellingConcerts.of(topConcerts);
    }

    /**
     * 특정 콘서트의 인기 순위 조회
     */
    public ConcertResult.ConcertRank getConcertRank(ConcertCriteria.GetRankOfConcert criteria) {
        Integer rank = concertSalesRankingService.getRankOfConcert(criteria.concertId());
        if (rank == null) {
            throw new RuntimeException("해당 콘서트의 랭킹 정보를 찾을 수 없습니다.");
        }
        return ConcertResult.ConcertRank.of(criteria.concertId(), rank);
    }
}
