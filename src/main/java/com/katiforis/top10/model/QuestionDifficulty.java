package com.katiforis.top10.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name= "question_difficulty")
public class QuestionDifficulty implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "description")
    private String description;

    public QuestionDifficulty() {
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
}
