package kr.hhplus.be.server.application;

import kr.hhplus.be.server.domain.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReserveSeatApplicationService implements ReserveSeatUseCase {
    private final ConcertSeatRepository concertSeatRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public Reservation reserveSeat(UUID concertSessionId, UUID seatId) {
        Seat seat = concertSeatRepository.findById(seatId);
        seat.reserveSeat();

        Reservation reservation = new Reservation(
                UUID.randomUUID(),
                seat.getConcertSessionId(),
                seatId,
                seat.getSeatPrice(),
                ReservationStatus.PENDING,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10)
        );
        reservationRepository.save(reservation);
        concertSeatRepository.save(seat);
        return reservation;
    }
}

