package com.katiforis.top10.DTO.game;

import com.katiforis.top10.util.GameResponseState;


public class ActiveGameAnswerDTO extends GameDTO {

    private long id;

    private GamePlayerDTO player;

    private AnswerDTO answer;

    private Boolean isCorrect;

    private Boolean hasAlreadyBeenSaid = false;

    private QuestionDTO question;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GamePlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(GamePlayerDTO player) {
        this.player = player;
    }

    public AnswerDTO getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerDTO answer) {
        this.answer = answer;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    public Boolean getHasAlreadyBeenSaid() {
        return hasAlreadyBeenSaid;
    }

    public void setHasAlreadyBeenSaid(Boolean hasAlreadyBeenSaid) {
        this.hasAlreadyBeenSaid = hasAlreadyBeenSaid;
    }

    public ActiveGameAnswerDTO() {
        super(GameResponseState.ANSWER.getState());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActiveGameAnswerDTO that = (ActiveGameAnswerDTO) o;

        return answer.equals(that.answer);
    }

    @Override
    public int hashCode() {
        return answer.hashCode();
    }
}
