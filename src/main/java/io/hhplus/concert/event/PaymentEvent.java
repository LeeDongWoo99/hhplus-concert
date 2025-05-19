package io.hhplus.concert.event;

import lombok.Getter;

@Getter
public class PaymentEvent {
    private final Long concertId;

    public PaymentEvent(Long concertId) {
        this.concertId = concertId;
    }
}
