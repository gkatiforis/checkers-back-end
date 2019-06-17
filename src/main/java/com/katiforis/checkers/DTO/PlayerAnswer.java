package com.katiforis.checkers.DTO;

import com.katiforis.checkers.game.Move;
import com.katiforis.checkers.DTO.response.GameResponse;
import com.katiforis.checkers.DTO.response.ResponseState;
import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PlayerAnswer extends GameResponse {
    private String description;
    private Integer points;
    private boolean isCorrect = false;
    private UserDto player;
    private Move move;
    private List<UserDto> players;

    public PlayerAnswer(String status, String gameId) {
        super(status, gameId);
    }
    public PlayerAnswer(){this.status = ResponseState.ANSWER.getState();}
}
