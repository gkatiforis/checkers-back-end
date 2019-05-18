package com.katiforis.top10.DTO.response;

import com.katiforis.top10.DTO.PlayerDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class GameStats extends GameResponse {

    List<PlayerDto> players;

    public GameStats(String gameId) {
        super(ResponseState.END_GAME.getState(), gameId);
    }
}
