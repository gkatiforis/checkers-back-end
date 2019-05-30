package com.katiforis.top10.service;

import com.katiforis.top10.DTO.UserDto;
import com.katiforis.top10.DTO.request.FindGame;
import com.katiforis.top10.DTO.response.GameState;

import java.util.List;

public interface GameHandlerService {

    void findGame(FindGame findGame);

    GameState createNewGame(List<UserDto> playerDtos);

    void endGame(String gameId);
}
