package com.katiforis.top10.DTO.game;

import com.katiforis.top10.model.QuestionDifficulty;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class QuestionDTO implements Serializable {
    private long id;
    private String description;
    private QuestionDifficulty questionDifficulty;
    private List<AnswerDTO> answers;

    public QuestionDTO(String description, QuestionDifficulty questionDifficulty, List<AnswerDTO> answers) {
        this.description = description;
        this.questionDifficulty = questionDifficulty;
        this.answers = answers;
    }
}
