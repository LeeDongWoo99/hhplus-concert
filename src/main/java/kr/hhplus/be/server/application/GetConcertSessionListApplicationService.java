package kr.hhplus.be.server.application;

import kr.hhplus.be.server.domain.Concert;
import kr.hhplus.be.server.domain.ConcertRepository;
import kr.hhplus.be.server.domain.ConcertSession;
import kr.hhplus.be.server.exception.ConcertNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetConcertSessionListApplicationService implements GetConcertSessionUseCase {

    private final ConcertRepository concertRepository;

    @Override
    public List<ConcertSession> getConcertSessionList(UUID concertId) {
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new ConcertNotFoundException(concertId));

        return concert.sessions();
    }
}
