package kr.hhplus.be.server.application;

import kr.hhplus.be.server.domain.Concert;
import kr.hhplus.be.server.domain.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetConcertListApplicationService implements GetConcertListUseCase{

    private final ConcertRepository concertRepository;

    @Override
    public List<Concert> getConcertList() {
        return concertRepository.findAll();
    }
}
