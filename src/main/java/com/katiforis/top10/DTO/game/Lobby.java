package com.katiforis.top10.DTO.game;

import com.katiforis.top10.model.Player;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Lobby extends Game {
    List<Player> players = new ArrayList<>();
    public Lobby(String status) {
        super(status);
    }
    public Lobby() {
        super(GameResponseState.LOBBY.getState());
    }
}
