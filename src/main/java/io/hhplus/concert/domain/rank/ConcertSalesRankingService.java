package io.hhplus.concert.domain.rank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertSalesRankingService {

    private final RankRepository rankRepository;

    public void increaseConcertSales(String concertId) {
        rankRepository.incrementScore(createSalesKey(), concertId);
    }

    public List<String> getTopSellingConcerts() {
        return rankRepository.findTopConcerts(createSalesKey(), 5);
    }

    public int getRankOfConcert(String concertId) {
        return rankRepository.getRank(createSalesKey(), concertId);
    }

    private String createSalesKey() {
        return "concert:sales:" + LocalDate.now();
    }
}
