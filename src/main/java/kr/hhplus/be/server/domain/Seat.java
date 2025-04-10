package kr.hhplus.be.server.domain;

import java.util.UUID;

public record Seat(UUID seatId,
                   UUID concertSessionId,
                   Long seatNumber,
                   String seatClass,
                   int seatPrice,
                   SeatStatus seatStatus) {
}

