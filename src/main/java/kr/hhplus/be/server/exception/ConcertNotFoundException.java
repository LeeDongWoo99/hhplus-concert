package kr.hhplus.be.server.exception;

import java.util.UUID;

public class ConcertNotFoundException extends RuntimeException {

    // 콘서트 ID를 받아서 예외에 대한 메시지를 생성
    public ConcertNotFoundException(UUID concertId) {
        super("Concert with id " + concertId + " not found.");
    }
}
