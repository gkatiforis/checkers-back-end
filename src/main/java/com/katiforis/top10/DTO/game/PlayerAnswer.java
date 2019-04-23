package com.katiforis.top10.DTO.game;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PlayerAnswer extends Game {
    private String description;
    private Integer points;
    private boolean isCorrect = false;
    private boolean hasAlreadyBeenSaid = false;
    private long questionId;
    private GamePlayer player;
    private String userId;

    public PlayerAnswer(String status, String answer, long questionId) {
        super(status);
        this.questionId = questionId;
    }

    public PlayerAnswer(String status, String gameId, String userId, String answer, long questionId) {
        super(status, gameId, userId);
        this.questionId = questionId;
    }

    public PlayerAnswer(String status, String userId, String answer, long questionId) {
        super(status, null, userId);
        this.questionId = questionId;
    }

    public PlayerAnswer(String status) {
        super(status);
    }
    public PlayerAnswer() {
        super(GameResponseState.ANSWER.getState());
    }
}
