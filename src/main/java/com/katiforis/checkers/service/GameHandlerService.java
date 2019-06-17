package com.katiforis.checkers.service;

import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.DTO.request.FindGame;
import com.katiforis.checkers.DTO.response.GameState;

import java.util.List;

public interface GameHandlerService {

    void findGame(FindGame findGame);

    GameState createNewGame(List<UserDto> playerDtos);

    void endGame(String gameId);

    void updateEndGameTime(String gameId, long timeSeconds);

    void getGameState(String gameId);
}
