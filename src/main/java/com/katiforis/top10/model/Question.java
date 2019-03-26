package com.katiforis.top10.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name= "question")
public class Question implements Serializable, Comparable<Question>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "description")
    private String description;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_difficulty_id")
    private QuestionDifficulty questionDifficulty;

    @OneToMany(mappedBy="question", fetch = FetchType.EAGER)
    private List<Answer> answers;

    public Question(String description, QuestionDifficulty questionDifficulty, List<Answer> answers) {
        this.description = description;
        this.questionDifficulty = questionDifficulty;
        this.answers = answers;
    }

    public Question() {
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

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        if (id != question.id) return false;
        if (!description.equals(question.description)) return false;
        if (!questionDifficulty.equals(question.questionDifficulty)) return false;
        return answers.equals(question.answers);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + description.hashCode();
        result = 31 * result + questionDifficulty.hashCode();
        result = 31 * result + answers.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", questionDifficulty=" + questionDifficulty +
                ", answers=" + answers +
                '}';
    }

    @Override
    public int compareTo(Question o) {
        Integer id = Integer.valueOf(((Long)questionDifficulty.getId()).intValue());
        Integer id2 = Integer.valueOf(((Long)o.getQuestionDifficulty().getId()).intValue());
        return id.compareTo(id2) ;
    }
}
