package com.katiforis.top10.DTO.response;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Start extends GameResponse {
        public Start(String gameId) {
        super(ResponseState.START_GAME.getState(), gameId);
    }
}
