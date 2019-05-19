package com.katiforis.top10.service;

import com.katiforis.top10.DTO.PlayerDto;
import com.katiforis.top10.DTO.request.FindGame;
import com.katiforis.top10.DTO.response.GameState;

import java.util.List;

public interface GameHandlerService {

    void findGame(FindGame findGame);

    GameState createNewGame(List<PlayerDto> playerDtos);

    void endGame(String gameId);
}
