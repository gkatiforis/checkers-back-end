package com.katiforis.top10.DTO.game;


public class PlayerAnswerDTO extends GameDTO {
    private String answer;
    private long questionId;

    public PlayerAnswerDTO(){}
    public PlayerAnswerDTO(String status, String answer, long questionId) {
        super(status);
        this.answer = answer;
        this.questionId = questionId;
    }

//    public PlayerAnswerDTO(String gameId, String status, String answer, long questionId) {
//        super(gameId, status);
//        this.answer = answer;
//        this.questionId = questionId;
//    }

    public PlayerAnswerDTO(String status, String gameId, String userId, String answer, long questionId) {
        super(status, gameId, userId);
        this.answer = answer;
        this.questionId = questionId;
    }

    public PlayerAnswerDTO(String status, String userId, String answer, long questionId) {
        super(status, null, userId);
        this.answer = answer;
        this.questionId = questionId;
    }

    public PlayerAnswerDTO(String status) {
        super(status);
    }

    public void setAnswer(String name) {
        this.answer = name;
    }

    public String getAnswer() {
        return answer;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }
}
