package kr.hhplus.be.server.application;

import java.util.UUID;

public interface ReserveSeatUseCase {

    Resservation reserveSeat(UUID concertSessionID, UUID seatID);
}
