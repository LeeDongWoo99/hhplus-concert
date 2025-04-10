package kr.hhplus.be.server.application;

import kr.hhplus.be.server.domain.Concert;
import kr.hhplus.be.server.domain.ConcertSession;

import java.util.List;
import java.util.UUID;

public interface GetConcertSessionUseCase {

    List<ConcertSession> getConcertSessionList(UUID ConcertId);
}
