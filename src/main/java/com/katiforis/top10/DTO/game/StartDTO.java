package com.katiforis.top10.DTO.game;

import com.katiforis.top10.util.GameResponseState;

public class StartDTO extends GameDTO {


    private String gameId;

    public StartDTO() {
        super(GameResponseState.START.getState());
    }

    public StartDTO(String gameId) {
        super(GameResponseState.START.getState());
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
