package com.katiforis.checkers.DTO.response;

import com.katiforis.checkers.DTO.UserDto;
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
