package com.katiforis.top10.util;

public enum GameResponseState {

    GAME_STATE("gamestate"),
    ANSWER("answer"),
    GAMEOVER("gameover"),
    START("start");

    public String state;

    GameResponseState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
