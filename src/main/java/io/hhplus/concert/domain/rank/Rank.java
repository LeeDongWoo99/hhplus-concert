package io.hhplus.concert.domain.rank;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rank {
    private String concertId;
    private LocalDate rankDate;
    private RankType rankType;
    private long score;

    @Builder
    private Rank(String concertId, LocalDate rankDate, RankType rankType, long score) {
        this.concertId = concertId;
        this.rankDate = rankDate;
        this.rankType = rankType;
        this.score = score;
    }

    public static Rank ofSell(String concertId, LocalDate date, long score) {
        return Rank.builder()
                .concertId(concertId)
                .rankDate(date)
                .rankType(RankType.SELL)
                .score(score)
                .build();
    }

    public String getRedisKey() {
        return "concert:sales:" + rankDate.toString();
    }
}
