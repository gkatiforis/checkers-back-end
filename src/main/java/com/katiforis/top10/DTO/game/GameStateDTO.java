package com.katiforis.top10.DTO.game;

import com.katiforis.top10.util.GameResponseState;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class GameStateDTO extends GameDTO {
    private List<GamePlayerDTO> players;
    private List<QuestionDTO> questions;
    private Date dateStarted;
    private Date currentDate;

    public GameStateDTO() {
            super(GameResponseState.GAME_STATE.getState());
    }
}
