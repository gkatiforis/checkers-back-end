package com.katiforis.top10.DTO.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class GameResponse extends BaseResponse implements Serializable {
    protected String gameId;

    public GameResponse(String status, String gameId) {
        super(status);
        this.gameId = gameId;
    }

    public GameResponse(String gameId, String status, String gameId1) {
        super(gameId, status);
        this.gameId = gameId1;
    }
}
