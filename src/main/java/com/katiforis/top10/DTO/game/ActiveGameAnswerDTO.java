package com.katiforis.top10.DTO.game;

import com.katiforis.top10.util.GameResponseState;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ActiveGameAnswerDTO extends GameDTO {
    private long id;
    private GamePlayerDTO player;
    private AnswerDTO answer;
    private Boolean isCorrect;
    private Boolean hasAlreadyBeenSaid = false;
    private QuestionDTO question;

    public ActiveGameAnswerDTO() {
        super(GameResponseState.ANSWER.getState());
    }
}
