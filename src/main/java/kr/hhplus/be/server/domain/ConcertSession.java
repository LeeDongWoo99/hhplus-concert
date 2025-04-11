package kr.hhplus.be.server.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ConcertSession(
        UUID sessionId,
        UUID concertId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        int availableSeats,
        List<Seat> seats
) {}
