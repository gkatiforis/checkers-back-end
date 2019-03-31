package com.katiforis.top10.DTO.game;

import com.katiforis.top10.util.GameResponseState;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PlayerAnswerDTO extends GameDTO {
    private String description;
    private Integer points;
    private boolean isCorrect = false;
    private boolean hasAlreadyBeenSaid = false;
    private long questionId;
    private GamePlayerDTO player;
    private String userId;

    public PlayerAnswerDTO(String status, String answer, long questionId) {
        super(status);
        this.questionId = questionId;
    }

    public PlayerAnswerDTO(String status, String gameId, String userId, String answer, long questionId) {
        super(status, gameId, userId);
        this.questionId = questionId;
    }

    public PlayerAnswerDTO(String status, String userId, String answer, long questionId) {
        super(status, null, userId);
        this.questionId = questionId;
    }

    public PlayerAnswerDTO(String status) {
        super(status);
    }
    public PlayerAnswerDTO() {
        super(GameResponseState.ANSWER.getState());
    }
}
