package com.katiforis.top10.DTO.game;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PlayerDTO implements Serializable {
    private long id;
    private String playerId;
    private String username;

    public PlayerDTO(String username) {
        this.username = username;
    }

    public PlayerDTO(String playerId, String username) {
        this.playerId = playerId;
        this.username = username;
    }
}
