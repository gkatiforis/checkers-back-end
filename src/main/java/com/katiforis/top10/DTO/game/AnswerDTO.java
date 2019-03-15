package com.katiforis.top10.DTO.game;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class AnswerDTO implements Serializable {

    private long id;
    private String description;
    private Integer points;
    @JsonIgnore
    private QuestionDTO question;

    public AnswerDTO() {
    }

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
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnswerDTO answerDTO = (AnswerDTO) o;

        return id == answerDTO.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
