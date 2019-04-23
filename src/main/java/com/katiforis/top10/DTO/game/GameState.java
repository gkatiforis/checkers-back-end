package com.katiforis.top10.DTO.game;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class GameState extends Game {
    private List<GamePlayer> players;
    private List<Question> questions;
    private Date dateStarted;
    private Date currentDate;

    public GameState() {
            super(GameResponseState.GAME_STATE.getState());
    }
}
