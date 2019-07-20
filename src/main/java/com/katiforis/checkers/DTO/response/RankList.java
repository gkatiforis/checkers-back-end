package com.katiforis.checkers.DTO.response;


import com.katiforis.checkers.DTO.UserDto;

import java.util.ArrayList;
import java.util.List;

public class RankList extends BaseResponse {
    List<UserDto> players = new ArrayList<>();
    UserDto currentPlayer;
    long currentPlayerPosition;

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

    public UserDto getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(UserDto currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setCurrentPlayerPosition(long currentPlayerPosition) {
        this.currentPlayerPosition = currentPlayerPosition;
    }

    public long getCurrentPlayerPosition() {
        return currentPlayerPosition;
    }
}
