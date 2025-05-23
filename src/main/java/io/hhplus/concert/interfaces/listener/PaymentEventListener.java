package io.hhplus.concert.interfaces.listener;

import io.hhplus.concert.domain.payment.PaymentCompletedEvent;
import io.hhplus.concert.domain.rank.ConcertSalesRankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {

    private final ConcertSalesRankingService concertSalesRankingService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentCompletedEvent(PaymentCompletedEvent event) {
        log.info("handlePaymentCompletedEvent 호출, concertId={}", event.getConcertId());
        concertSalesRankingService.increaseConcertSales(String.valueOf(event.getConcertId())
        );
    }
}