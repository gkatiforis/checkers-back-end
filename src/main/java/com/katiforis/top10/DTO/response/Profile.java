package com.katiforis.top10.DTO.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Profile extends BaseResponse {
    private String username;
    public Profile(String status) {
        super(status);
    }
    public Profile() {
        super(ResponseState.PLAYER_DETAILS.getState());
    }
}
