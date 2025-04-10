package kr.hhplus.be.server.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConcertRepository {

    List<Concert> findAll();

    Optional<Concert> findById(UUID concertId);
}
