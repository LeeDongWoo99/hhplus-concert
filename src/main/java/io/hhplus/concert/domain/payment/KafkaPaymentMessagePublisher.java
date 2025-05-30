package io.hhplus.concert.domain.payment;

public interface KafkaPaymentMessagePublisher {
    void publish(PaymentCompletedMessage message);
}
