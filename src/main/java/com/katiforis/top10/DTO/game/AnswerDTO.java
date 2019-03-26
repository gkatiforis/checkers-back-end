package com.katiforis.top10.DTO.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = "question")
@ToString
public class AnswerDTO implements Serializable {
    private long id;
    private String description;
    private Integer points;
    @JsonIgnore
    private QuestionDTO question;

      public AnswerDTO(String description) {
        this.id = 0;
        this.description = description;
        this.points = 0;
        this.question = null;
    }

    public AnswerDTO(String description, Integer points, QuestionDTO question) {
        this.description = description;
        this.points = points;
        this.question = question;
    }

    public AnswerDTO(String description, Integer points) {
        this.description = description;
        this.points = points;
    }

    public AnswerDTO(long id, String description, Integer points) {
        this.id =id;
        this.description = description;
        this.points = points;
    }
}
