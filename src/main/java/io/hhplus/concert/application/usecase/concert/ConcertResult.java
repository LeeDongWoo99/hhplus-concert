package io.hhplus.concert.application.usecase.concert;

import java.util.List;

public class ConcertResult {

    public record TopSellingConcerts(List<String> concertIds) { // 파라미터 이름 수정 필요
        public static TopSellingConcerts of(List<String> concertIds) {
            return new TopSellingConcerts(concertIds);
        }
    }

    public record ConcertRank(String concertId, long rank) {
        public static ConcertRank of(String concertId, long rank) {
            return new ConcertRank(concertId, rank);
        }
    }
}
