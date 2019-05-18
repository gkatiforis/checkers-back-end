package com.katiforis.top10.DTO.response;


import com.katiforis.top10.DTO.PlayerDto;

import java.util.ArrayList;
import java.util.List;

public class RankList extends BaseResponse {
    List<PlayerDto> playerDtos = new ArrayList<>();

    public RankList(String status) {
        super(status);
    }

    public RankList() {
        super(ResponseState.RANK_LIST.getState());
    }
    public RankList(String gameId, String status) {
        super(gameId, status);
    }

    public List<PlayerDto> getPlayerDtos() {
        return playerDtos;
    }

    public void setPlayerDtos(List<PlayerDto> playerDtos) {
        this.playerDtos = playerDtos;
    }
}
