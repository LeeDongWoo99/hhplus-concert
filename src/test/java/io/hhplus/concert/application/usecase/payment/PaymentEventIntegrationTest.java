package io.hhplus.concert.application.usecase.payment;

import io.hhplus.concert.domain.payment.PaymentCompletedEvent;
import io.hhplus.concert.domain.rank.RankRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class PaymentEventIntegrationTest {

    @Autowired
    RankRepository rankRepository;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Test
    void 결제_이벤트_발생시_점수가_증가해야_한다() {
        // given
        String redisKey = "concert:ranking";
        String concertId = "1";

        Double prevScore = Optional.ofNullable(rankRepository.getScore(redisKey, concertId)).orElse(0.0);

        PaymentCompletedEvent event = PaymentCompletedEvent.builder()
                .concertName("testConcert")
                .concertId(Long.parseLong(concertId))
                .concertDateId(1L)
                .seatId(1L)
                .amount(10000L)
                .build();

        // when
        eventPublisher.publishEvent(event);

        // then
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            Double newScore = Optional.ofNullable(rankRepository.getScore(redisKey, concertId)).orElse(0.0);
            assertThat(newScore).isEqualTo(prevScore + 1.0);
        });
    }
}

