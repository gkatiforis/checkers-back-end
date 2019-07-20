package com.katiforis.checkers.DTO.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class OfferDraw extends GameResponse {
        String byUser;
        public OfferDraw(String gameId) {
        super(ResponseState.OFFER_DRAW.getState(), gameId);
    }
}
