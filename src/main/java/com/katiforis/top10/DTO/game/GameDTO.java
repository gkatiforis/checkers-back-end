package com.katiforis.top10.DTO.game;

import java.io.Serializable;


public abstract class GameDTO implements Serializable {
    protected String status;
    protected String gameId;
    protected String userId;

    public GameDTO(){}
    public GameDTO(String status) {
        this.status = status;
    }

    public GameDTO(String gameId, String status) {
        this.gameId = gameId;
        this.status = status;
    }

    public GameDTO(String status, String gameId, String userId) {
        this.status = status;
        this.gameId = gameId;
        this.userId = userId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
