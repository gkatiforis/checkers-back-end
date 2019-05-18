package com.katiforis.top10.DTO.response;


import com.katiforis.top10.DTO.PlayerDto;

import java.util.ArrayList;
import java.util.List;

public class RankList extends BaseResponse {
    List<PlayerDto> players = new ArrayList<>();

    public RankList(String status) {
        super(status);
    }

    public RankList() {
        super(ResponseState.RANK_LIST.getState());
    }
    public RankList(String gameId, String status) {
        super(gameId, status);
    }

    public List<PlayerDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDto> players) {
        this.players = players;
    }
}
