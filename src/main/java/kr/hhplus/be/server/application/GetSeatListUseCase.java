package kr.hhplus.be.server.application;

import kr.hhplus.be.server.domain.Seat;

import java.util.List;
import java.util.UUID;

public interface GetSeatListUseCase {

    List<Seat> getSeatList(UUID ConcertSessionId);
}
