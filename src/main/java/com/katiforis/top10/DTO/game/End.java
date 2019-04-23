package com.katiforis.top10.DTO.game;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class End extends Game {
    public End(String gameId) {
        super(gameId, GameResponseState.END_GAME.getState());
    }
}
