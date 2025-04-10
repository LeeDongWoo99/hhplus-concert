package kr.hhplus.be.server.application;

import kr.hhplus.be.server.domain.Concert;

import java.util.List;

public interface GetConcertListUseCase {

    List<Concert> getConcertList();
}
