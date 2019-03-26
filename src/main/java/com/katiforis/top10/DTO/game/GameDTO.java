package com.katiforis.top10.DTO.game;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class GameDTO implements Serializable {
    protected String status;
    protected String gameId;
    protected String userId;

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
}
