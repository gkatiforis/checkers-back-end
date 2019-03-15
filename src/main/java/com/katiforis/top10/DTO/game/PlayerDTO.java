package com.katiforis.top10.DTO.game;

import java.io.Serializable;


public class PlayerDTO implements Serializable {
    private long id;
    private String playerId;
    private String username;

    public PlayerDTO() {
    }

    public PlayerDTO(String username) {
        this.username = username;
    }

    public PlayerDTO(String playerId, String username) {
        this.playerId = playerId;
        this.username = username;
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
}
