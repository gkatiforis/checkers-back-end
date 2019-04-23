package com.katiforis.top10.DTO.game;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Player implements Serializable {
    private long id;
    private String playerId;
    private String username;

    public Player(String username) {
        this.username = username;
    }

    public Player(String playerId, String username) {
        this.playerId = playerId;
        this.username = username;
    }
}
