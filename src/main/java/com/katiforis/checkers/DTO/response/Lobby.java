package com.katiforis.checkers.DTO.response;

import com.katiforis.checkers.model.User;
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
public class Lobby extends BaseResponse {
    List<User> users = new ArrayList<>();
    public Lobby(String status) {
        super(status);
    }
    public Lobby() {
        super(ResponseState.LOBBY.getState());
    }
}
