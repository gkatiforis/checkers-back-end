package com.katiforis.top10.DTO.game;

public enum GameResponseState {

    GAME_STATE("game_state"),
    ANSWER("answer"),
    END_GAME("end_game"),
    START_GAME("start_game"),
    FRIEND_LIST("friend_list"),
    LOBBY("lobby"),
    NOTIFICATION_LIST("notification_list"),
    PLAYER_DETAILS("player_details");

    public String state;

    GameResponseState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
