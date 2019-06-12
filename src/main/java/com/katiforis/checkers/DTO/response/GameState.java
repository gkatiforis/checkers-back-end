package com.katiforis.checkers.DTO.response;

import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.game.Board;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class GameState extends GameResponse {
    private int gameStatus;
    private List<UserDto> players;
    private UserDto currentPlayer;
    private Board board;
    private Date dateStarted;
    private Date currentDate;

    public GameState(String gameId) {
            super(ResponseState.GAME_STATE.getState(), gameId);
    }

    public static class Status {
        public static final int PLAYERS_SELECTION = 1;
        public static final int IN_PROGRESS = 2;
        public static final int TERMINATED = 3;
    }
}