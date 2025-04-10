package kr.hhplus.be.server.exception;

import java.util.UUID;

public class ConcertSessionNotFoundException extends RuntimeException {

    public ConcertSessionNotFoundException(UUID concertSessionId) {
        super("Concert session with id " + concertSessionId + " not found.");
    }
}
