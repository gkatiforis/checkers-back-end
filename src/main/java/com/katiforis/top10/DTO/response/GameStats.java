package com.katiforis.top10.DTO.response;

import com.katiforis.top10.DTO.UserDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class GameStats extends GameResponse {

    List<UserDto> players;

    public GameStats(String gameId) {
        super(ResponseState.END_GAME.getState(), gameId);
    }
}
