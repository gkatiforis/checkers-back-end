package com.katiforis.top10.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name= "answer")
public class Answer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "display_description")
    private String displayDescription;

//    @Basic
//    @Column(name = "identical")
//    private Boolean identical;

    @Basic
    @Column(name = "points")
    private Integer points;

    @ManyToOne(fetch = FetchType.LAZY)
//    @PrimaryKeyJoinColumn
    @JoinColumn(name = "question_id")
    private Question question;

//    @ManyToOne
//    @JoinColumn(name="active_game_id", nullable=false)
//    private ActiveGame activeGame;

    public Answer() {
    }

//    public Answer(String description, Integer points, Question question) {
//        this.description = description;
//        this.points = points;
//        this.question = question;
//    }

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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getDisplayDescription() {
        return displayDescription;
    }

    public void setDisplayDescription(String displayDescription) {
        this.displayDescription = displayDescription;
    }
//
//    public Boolean getIdentical() {
//        return identical;
//    }
//
//    public void setIdentical(Boolean identical) {
//        this.identical = identical;
//    }
}