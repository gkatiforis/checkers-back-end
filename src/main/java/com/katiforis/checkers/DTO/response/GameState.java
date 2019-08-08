package com.katiforis.checkers.DTO.response;

import com.katiforis.checkers.DTO.GameType;
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
    private GameType gameType;
    private Board board;
    private Date dateStarted;
    private Date currentDate;
    private Date lastMoveDate;
    private Integer gameMaxTime;
    private String resignUserId;
    private String offerDrawUserId;
    private Date offerDrawDate = new Date();
    private boolean draw;
    private GameStats gameStats;

    public GameState(String gameId) {
            super(ResponseState.GAME_STATE.getState(), gameId);
    }


    public static class Status {
        public static final int PLAYERS_SELECTION = 1;
        public static final int IN_PROGRESS = 2;
        public static final int TERMINATED = 3;
    }

    public UserDto getCurrentPlayer(){
        if(players == null || players.isEmpty()){
            return null;
        }
        if(players.get(0).getIsCurrent()){
            return players.get(0);
        }else{
            return players.get(1);
        }
    }
}
