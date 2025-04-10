package kr.hhplus.be.server.domain;

import java.util.Optional;
import java.util.UUID;

public interface ConcertSessionRepository {

    Optional<ConcertSession> findById(UUID concertSessionId);
}
