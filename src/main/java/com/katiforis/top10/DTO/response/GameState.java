package com.katiforis.top10.DTO.response;

import com.katiforis.top10.DTO.UserDto;
import com.katiforis.top10.DTO.Question;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class GameState extends GameResponse {
    private List<UserDto> players;
    private List<Question> questions;
    private Date dateStarted;
    private Date currentDate;

    public GameState(String gameId) {
            super(ResponseState.GAME_STATE.getState(), gameId);
    }
}
