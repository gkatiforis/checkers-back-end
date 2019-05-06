package com.katiforis.top10.DTO.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PlayerDetails extends BaseResponse {
    private String username;
    public PlayerDetails(String status) {
        super(status);
    }
    public PlayerDetails() {
        super(ResponseState.PLAYER_DETAILS.getState());
    }
}
