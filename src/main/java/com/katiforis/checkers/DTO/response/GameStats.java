package com.katiforis.checkers.DTO.response;

import com.katiforis.checkers.DTO.UserDto;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class GameStats extends GameResponse {
    private String winnerColor;
    private List<UserDto> players;
    private boolean draw;
    private Date gameEndDate;
    private Date currentDate;

    public GameStats(String gameId) {
        super(ResponseState.END_GAME.getState(), gameId);
    }
}
