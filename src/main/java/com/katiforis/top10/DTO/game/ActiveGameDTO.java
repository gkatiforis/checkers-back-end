package com.katiforis.top10.DTO.game;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class ActiveGameDTO implements Serializable {

    private long id;
    private Integer active_player;
    private Date date_started;
    List<QuestionDTO> questions;
    List<PlayerDTO> players;
    List<ActiveGameAnswerDTO> activeGameAnswers;

    public ActiveGameDTO() {
    }

    public ActiveGameDTO(Date date_started) {
        this.date_started = date_started;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getActive_player() {
        return active_player;
    }

    public void setActive_player(Integer active_player) {
        this.active_player = active_player;
    }

    public Date getDate_started() {
        return date_started;
    }

    public void setDate_started(Date date_started) {
        this.date_started = date_started;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
    }

    public List<ActiveGameAnswerDTO> getActiveGameAnswers() {
        return activeGameAnswers;
    }

    public void setActiveGameAnswers(List<ActiveGameAnswerDTO> activeGameAnswers) {
        this.activeGameAnswers = activeGameAnswers;
    }
}
