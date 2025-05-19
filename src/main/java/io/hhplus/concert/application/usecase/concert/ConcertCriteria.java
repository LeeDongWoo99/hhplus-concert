package io.hhplus.concert.application.usecase.concert;

import io.hhplus.concert.interfaces.api.common.InvalidValidationException;

import static io.hhplus.concert.interfaces.api.user.CommonErrorCode.CONCERT_RANK_NOT_FOUND;

public class ConcertCriteria {

    public record GetRankOfConcert(String concertId) {
        public static GetRankOfConcert of(String concertId) {
            if (concertId == null || concertId.trim().isEmpty()) {
                throw new InvalidValidationException(CONCERT_RANK_NOT_FOUND);
            }
            return new GetRankOfConcert(concertId);
        }
    }
}
