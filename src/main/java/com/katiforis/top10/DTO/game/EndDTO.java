package com.katiforis.top10.DTO.game;

import com.katiforis.top10.util.GameResponseState;


public class EndDTO extends GameDTO {

    public EndDTO(String gameId) {
        super(gameId, GameResponseState.GAMEOVER.getState());
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
