package com.katiforis.top10.DTO;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class PlayerDetailsDto implements Serializable {

    private int elo;
    private int level;
    private int levelPoints;
    private int coins;
    private int eloExtra;
    private int levelExtra;
    private int levelPointsExtra;
    private int coinsExtra;

    public PlayerDetailsDto(int elo, int level, int levelPoints, int coins, int eloExtra, int levelExtra, int levelPointsExtra, int coinsExtra) {
        this.elo = elo;
        this.level = level;
        this.levelPoints = levelPoints;
        this.coins = coins;
        this.eloExtra = eloExtra;
        this.levelExtra = levelExtra;
        this.levelPointsExtra = levelPointsExtra;
        this.coinsExtra = coinsExtra;
    }
}
