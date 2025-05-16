package io.hhplus.concert.interfaces.listener;

import io.hhplus.concert.event.PaymentEvent;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PaymentEventListener {

    private final RedissonClient redissonClient;

    @Autowired
    public PaymentEventListener(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentCompletedEvent(PaymentEvent event) {
        RScoredSortedSet<Object> set = redissonClient.getScoredSortedSet("concert:sales:");
        set.add(1, event.getConcertId().toString());
    }
}