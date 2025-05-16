package io.hhplus.concert.domain.concert;

import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.protocol.ScoredEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConcertSalesRankingService {

    private final RedissonClient redissonClient;

    @Autowired
    public ConcertSalesRankingService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public List<String> getTopSellingConcerts() {
        RScoredSortedSet<String> set = redissonClient.getScoredSortedSet("concert:sales:");
        if(set.isExists()) {
            return set.entryRangeReversed(0, 4).stream()
                    .map(ScoredEntry::getValue)  // Entry가 아니라 ScoredEntry!
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("순위를 매길 수 있는 콘서트가 없습니다.");
        }
    }

    public Integer getRankOfConcert(String concertId) {
        RScoredSortedSet<String> set = redissonClient.getScoredSortedSet("concert:sales:");
        if(set.isExists()) {
            return set.revRank(concertId) + 1; // 순위는 1부터 시작하므로, +1을 추가한다.
        } else {
            throw new RuntimeException("해당 콘서트의 판매 순위를 구할 수 없습니다.");
        }
    }
}

