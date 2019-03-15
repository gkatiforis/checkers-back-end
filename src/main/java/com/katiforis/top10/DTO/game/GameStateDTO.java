package com.katiforis.top10.DTO.game;

import com.katiforis.top10.util.GameResponseState;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class GameStateDTO extends GameDTO {
    private List<GamePlayerDTO> players;
    private List<QuestionDTO> questions;
    private Set<ActiveGameAnswerDTO> currentAnswers;
    private Date dateStarted;
    private Date currentDate;

    public GameStateDTO() {
            super(GameResponseState.GAME_STATE.getState());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public List<GamePlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<GamePlayerDTO> players) {
        this.players = players;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    public Set<ActiveGameAnswerDTO> getCurrentAnswers() {
        return currentAnswers;
    }

    public void setCurrentAnswers(Set<ActiveGameAnswerDTO> currentAnswers) {
        this.currentAnswers = currentAnswers;
    }

    public Date getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(Date dateStarted) {
        this.dateStarted = dateStarted;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameStateDTO that = (GameStateDTO) o;

        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (players != null ? !players.equals(that.players) : that.players != null) return false;
        if (questions != null ? !questions.equals(that.questions) : that.questions != null) return false;
        if (currentAnswers != null ? !currentAnswers.equals(that.currentAnswers) : that.currentAnswers != null)
            return false;
        if (dateStarted != null ? !dateStarted.equals(that.dateStarted) : that.dateStarted != null) return false;
        return currentDate != null ? currentDate.equals(that.currentDate) : that.currentDate == null;
    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (players != null ? players.hashCode() : 0);
        result = 31 * result + (questions != null ? questions.hashCode() : 0);
        result = 31 * result + (currentAnswers != null ? currentAnswers.hashCode() : 0);
        result = 31 * result + (dateStarted != null ? dateStarted.hashCode() : 0);
        result = 31 * result + (currentDate != null ? currentDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GameStateDTO{" +
                "status='" + status + '\'' +
                ", players=" + players +
                ", questions=" + questions +
                ", currentAnswers=" + currentAnswers +
                ", dateStarted=" + dateStarted +
                ", currentDate=" + currentDate +
                '}';
    }
}
