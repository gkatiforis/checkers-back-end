package com.katiforis.top10.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
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

    @Override
    public int compareTo(Question o) {
        Integer id = Integer.valueOf(((Long)questionDifficulty.getId()).intValue());
        Integer id2 = Integer.valueOf(((Long)o.getQuestionDifficulty().getId()).intValue());
        return id.compareTo(id2) ;
    }
}
