package com.katiforis.top10.DTO.game;

import com.katiforis.top10.model.QuestionDifficulty;

import java.io.Serializable;
import java.util.Set;

public class QuestionDTO implements Serializable {

    private long id;

    private String description;

    private QuestionDifficulty questionDifficulty;
    private Set<AnswerDTO> answers;

    public QuestionDTO(String description, QuestionDifficulty questionDifficulty, Set<AnswerDTO> answers) {
        this.description = description;
        this.questionDifficulty = questionDifficulty;
        this.answers = answers;
    }

    public QuestionDTO() {
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

    public QuestionDifficulty getQuestionDifficulty() {
        return questionDifficulty;
    }

    public void setQuestionDifficulty(QuestionDifficulty questionDifficulty) {
        this.questionDifficulty = questionDifficulty;
    }

    public Set<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<AnswerDTO> answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionDTO that = (QuestionDTO) o;

        if (id != that.id) return false;
        if (!description.equals(that.description)) return false;
        if (!questionDifficulty.equals(that.questionDifficulty)) return false;
        return answers.equals(that.answers);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + description.hashCode();
        result = 31 * result + questionDifficulty.hashCode();
        result = 31 * result + answers.hashCode();
        return result;
    }
}
