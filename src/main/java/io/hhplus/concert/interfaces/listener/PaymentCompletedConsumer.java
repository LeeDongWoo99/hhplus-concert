package io.hhplus.concert.interfaces.listener;

import io.hhplus.concert.domain.payment.PaymentCompletedMessage;
import io.hhplus.concert.domain.rank.ConcertSalesRankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCompletedConsumer {

    private final ConcertSalesRankingService rankingService;

    @KafkaListener(topics = "payment-completed", groupId = "payment-consumer-group")
    public void consume(PaymentCompletedMessage message) {

        // 인기 콘서트 집계 처리
        log.info("handlePaymentCompletedEvent 호출, concertId={}", message.concertId());
        rankingService.increaseConcertSales(String.valueOf(message.concertId()));
    }
}
