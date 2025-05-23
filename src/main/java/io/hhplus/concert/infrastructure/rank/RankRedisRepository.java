package io.hhplus.concert.infrastructure.rank;

import io.hhplus.concert.domain.rank.Rank;
import io.hhplus.concert.domain.rank.RankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RankRedisRepository implements RankRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void incrementScore(String key, String concertId) {
        log.info("increaseConcertSales 호출, concertId={}", concertId);
        redisTemplate.opsForZSet().incrementScore(key, concertId, 1);

    }

    @Override
    public List<String> findTopConcerts(String redisKey, int top) {
        Set<ZSetOperations.TypedTuple<String>> result = redisTemplate.opsForZSet()
                .reverseRangeWithScores(redisKey, 0, top - 1);

        if (result.isEmpty()) return List.of();

        return result.stream()
                .map(TypedTuple::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public int getRank(String redisKey, String concertId) {
        Long rank = redisTemplate.opsForZSet().reverseRank(redisKey, concertId);
        if (rank == null) throw new RuntimeException("해당 콘서트의 순위를 찾을 수 없습니다.");
        return rank.intValue() + 1;
    }

    @Override
    public Double getScore(String key, String concertId) {
        return redisTemplate.opsForZSet().score(key, concertId);
    }
}
