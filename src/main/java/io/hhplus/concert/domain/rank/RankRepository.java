package io.hhplus.concert.domain.rank;

import java.util.List;

public interface RankRepository {
    void  incrementScore(String key, String concertId);
    List<String> findTopConcerts(String redisKey, int top);
    int getRank(String redisKey, String concertId);
    Double getScore(String redisKey, String concertId);
}
