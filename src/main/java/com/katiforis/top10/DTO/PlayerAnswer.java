package com.katiforis.top10.DTO;

import com.katiforis.top10.DTO.response.GameResponse;
import com.katiforis.top10.DTO.response.ResponseState;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PlayerAnswer extends GameResponse {
    private String description;
    private Integer points;
    private boolean isCorrect = false;
    private boolean hasAlreadyBeenSaid = false;
    private long questionId;
    private UserDto player;

    public PlayerAnswer(String status, String gameId) {
        super(status, gameId);
    }
    public PlayerAnswer(){this.status = ResponseState.ANSWER.getState();}
}
