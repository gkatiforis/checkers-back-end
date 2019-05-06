package com.katiforis.top10.DTO.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class End extends GameResponse {
    public End(String gameId) {
        super(gameId, ResponseState.END_GAME.getState());
    }
}
