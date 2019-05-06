package com.katiforis.top10.DTO;

import com.katiforis.top10.model.QuestionDifficulty;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Question implements Serializable {
    private long id;
    private String description;
    private QuestionDifficulty questionDifficulty;
    private List<Answer> answers;
    private Set<PlayerAnswer> currentAnswers = new HashSet<>();

    public Question(String description, QuestionDifficulty questionDifficulty, List<Answer> answers) {
        this.description = description;
        this.questionDifficulty = questionDifficulty;
        this.answers = answers;
    }
}
