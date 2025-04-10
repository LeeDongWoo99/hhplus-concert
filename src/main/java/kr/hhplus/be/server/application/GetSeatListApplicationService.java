package kr.hhplus.be.server.application;

import kr.hhplus.be.server.domain.ConcertSession;
import kr.hhplus.be.server.domain.ConcertSessionRepository;
import kr.hhplus.be.server.domain.Seat;
import kr.hhplus.be.server.exception.ConcertSessionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetSeatListApplicationService implements GetSeatListUseCase {

    private final ConcertSessionRepository concertSessionRepository;

    @Override
    public List<Seat> getSeatList(UUID concertSessionId) {
        ConcertSession concertSession = concertSessionRepository.findById(concertSessionId)
                .orElseThrow(() -> new ConcertSessionNotFoundException(concertSessionId));

        return concertSession.seats();
    }
}

