package com.katiforis.checkers.DTO.response;


import com.katiforis.checkers.DTO.UserDto;

import java.util.ArrayList;
import java.util.List;

public class RankList extends BaseResponse {
    List<UserDto> players = new ArrayList<>();

    public RankList(String status) {
        super(status);
    }

    public RankList() {
        super(ResponseState.RANK_LIST.getState());
    }
    public RankList(String gameId, String status) {
        super(gameId, status);
    }

    public List<UserDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<UserDto> players) {
        this.players = players;
    }
}
