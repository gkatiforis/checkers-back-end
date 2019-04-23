package com.katiforis.top10.DTO.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PlayerDetails extends Game {
    private String username;
    public PlayerDetails(String status) {
        super(status);
    }
    public PlayerDetails() {
        super(GameResponseState.PLAYER_DETAILS.getState());
    }
}
