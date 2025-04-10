package kr.hhplus.be.server.application;

import kr.hhplus.be.server.domain.Concert;
import kr.hhplus.be.server.domain.ConcertRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetConcertListApplicationService implements GetConcertListUseCase{

    private final ConcertRepository concertRepository;

    public GetConcertListApplicationService(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    @Override
    public List<Concert> getConcertList() {
        return concertRepository.findAll();
    }
}
