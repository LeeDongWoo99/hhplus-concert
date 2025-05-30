package io.hhplus.concert.domain.payment;

public record PaymentCompletedMessage(
        String concertName,
        long concertId,
        long concertDateId,
        long seatId,
        long price
) {}
