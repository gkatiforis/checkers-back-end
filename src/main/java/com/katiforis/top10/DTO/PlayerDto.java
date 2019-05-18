package com.katiforis.top10.DTO;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PlayerDto extends User implements Serializable {
    private String playerId;
    private PlayerDetailsDto playerDetails;

    public PlayerDto(String username) {
        super(username);
    }
    public PlayerDto(String playerId, String username) {
        super(username);
        this.playerId = playerId;
    }

    public PlayerDto(String playerId, PlayerDetailsDto playerDetails) {
        this.playerId = playerId;
        this.playerDetails = playerDetails;
    }

    public PlayerDto(String username, String playerId, PlayerDetailsDto playerDetails) {
        super(username);
        this.playerId = playerId;
        this.playerDetails = playerDetails;
    }

    public PlayerDto(long id, String username, String imageUrl, String playerId, PlayerDetailsDto playerDetails) {
        super(id, username, imageUrl);
        this.playerId = playerId;
        this.playerDetails = playerDetails;
    }
}
