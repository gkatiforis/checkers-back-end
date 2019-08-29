package com.katiforis.checkers.service;

import com.katiforis.checkers.DTO.GameType;
import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.DTO.request.FindGame;
import com.katiforis.checkers.DTO.response.GameState;
import com.katiforis.checkers.exception.GameException;
import com.katiforis.checkers.model.User;

import java.util.List;

public interface GameHandlerService {

    void findGame(FindGame findGame) throws GameException;

    GameState createNewGame(List<UserDto> playerDtos, GameType gameType);

    void payFee(User user);

    void checkFee(User user) throws GameException;

    void endGame(String gameId);

    void updateEndGameTime(String gameId, long timeSeconds);

    void getGameState(String gameId);
}
