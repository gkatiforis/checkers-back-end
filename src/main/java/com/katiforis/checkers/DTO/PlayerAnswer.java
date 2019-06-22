package com.katiforis.checkers.DTO;

import com.katiforis.checkers.game.Move;
import com.katiforis.checkers.DTO.response.GameResponse;
import com.katiforis.checkers.DTO.response.ResponseState;
import lombok.*;

import java.util.List;


@EqualsAndHashCode
@ToString
public class PlayerAnswer extends GameResponse {
    private UserDto player;
    private Move move;
    private List<UserDto> players;

    private String message;
    private Boolean resign;
    private Boolean offerDraw;

    public PlayerAnswer(String status, String gameId) {
        super(status, gameId);
    }
    public PlayerAnswer(){this.status = ResponseState.ANSWER.getState();}

    public UserDto getPlayer() {
        return player;
    }

    public void setPlayer(UserDto player) {
        this.player = player;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public List<UserDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<UserDto> players) {
        this.players = players;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getResign() {
        if(resign == null)return false;
        return resign;
    }

    public void setResign(Boolean resign) {
        this.resign = resign;
    }

    public Boolean getOfferDraw() {
        if(offerDraw == null)return false;
        return offerDraw;
    }

    public void setOfferDraw(Boolean offerDraw) {
        this.offerDraw = offerDraw;
    }
}
