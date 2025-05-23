package io.hhplus.concert.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class PaymentCompletedEvent {
    private final String concertName;
    private final long concertId;
    private final long concertDateId;
    private final long seatId;
    private final long amount;
}
