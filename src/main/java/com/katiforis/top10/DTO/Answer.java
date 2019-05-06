package com.katiforis.top10.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = "question")
@ToString
public class Answer implements Serializable {
    private long id;
    private String displayDescription;
    private String description;
    private Integer points;
    @JsonIgnore
    private Question question;

      public Answer(String description) {
        this.id = 0;
        this.description = description;
        this.points = 0;
        this.question = null;
    }

    public Answer(String description, Integer points, Question question) {
        this.description = description;
        this.points = points;
        this.question = question;
    }

    public Answer(String description, Integer points) {
        this.description = description;
        this.points = points;
    }

    public Answer(long id, String description, Integer points) {
        this.id =id;
        this.description = description;
        this.points = points;
    }
}
