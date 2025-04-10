package kr.hhplus.be.server.domain;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record Concert(
        UUID concertId,
        String name,
        String location,
        LocalDate startDate,
        LocalDate endDate,
        List<ConcertSession> sessions
) {

}
