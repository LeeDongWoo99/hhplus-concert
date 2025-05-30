package io.hhplus.concert.infrastructure.persistence.payment;

import io.hhplus.concert.domain.payment.PaymentCompletedMessage;
import io.hhplus.concert.domain.payment.KafkaPaymentMessagePublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaPaymentPublisher implements KafkaPaymentMessagePublisher {

    private final KafkaTemplate<String, PaymentCompletedMessage> kafkaTemplate;

    public KafkaPaymentPublisher(KafkaTemplate<String, PaymentCompletedMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(PaymentCompletedMessage message) {
        kafkaTemplate.send("payment-completed", message);
    }
}
