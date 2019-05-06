package com.katiforis.top10.DTO;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class GamePlayer implements Serializable {
    private long id;
    private String playerId;
    private String username;
    private int points;

    public GamePlayer(String username) {
        this.username = username;
    }

    public GamePlayer(String playerId, String username) {
        this.playerId = playerId;
        this.username = username;
    }

    public GamePlayer(String playerId, String username, int points) {
        this.playerId = playerId;
        this.username = username;
        this.points = points;
    }
}
