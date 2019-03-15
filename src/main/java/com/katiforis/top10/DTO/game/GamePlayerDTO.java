package com.katiforis.top10.DTO.game;

import java.io.Serializable;


public class GamePlayerDTO implements Serializable {
    private long id;
    private String playerId;
    private String username;
    private int points;


    public GamePlayerDTO() {
    }

    public GamePlayerDTO(String username) {
        this.username = username;
    }

    public GamePlayerDTO(String playerId, String username) {
        this.playerId = playerId;
        this.username = username;
    }

    public GamePlayerDTO(String playerId, String username, int points) {
        this.playerId = playerId;
        this.username = username;
        this.points = points;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
