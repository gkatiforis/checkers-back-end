package com.katiforis.top10.DTO.game;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Start extends Game {
    private String gameId;

    public Start() {
        super(GameResponseState.START_GAME.getState());
    }

    public Start(String gameId) {
        super(GameResponseState.START_GAME.getState());
        this.gameId = gameId;
    }
}
