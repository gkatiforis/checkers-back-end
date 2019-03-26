package com.katiforis.top10.DTO.game;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PlayerAnswerDTO extends GameDTO {
    private String answer;
    private long questionId;

    public PlayerAnswerDTO(String status, String answer, long questionId) {
        super(status);
        this.answer = answer;
        this.questionId = questionId;
    }

    public PlayerAnswerDTO(String status, String gameId, String userId, String answer, long questionId) {
        super(status, gameId, userId);
        this.answer = answer;
        this.questionId = questionId;
    }

    public PlayerAnswerDTO(String status, String userId, String answer, long questionId) {
        super(status, null, userId);
        this.answer = answer;
        this.questionId = questionId;
    }

    public PlayerAnswerDTO(String status) {
        super(status);
    }
}
