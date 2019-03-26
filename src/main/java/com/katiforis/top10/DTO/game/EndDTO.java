package com.katiforis.top10.DTO.game;

import com.katiforis.top10.util.GameResponseState;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class EndDTO extends GameDTO {
    public EndDTO(String gameId) {
        super(gameId, GameResponseState.GAMEOVER.getState());
    }
}
