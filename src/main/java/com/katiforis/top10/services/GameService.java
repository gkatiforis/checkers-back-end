package com.katiforis.top10.services;

import com.katiforis.top10.DTO.game.PlayerAnswerDTO;
import com.katiforis.top10.DTO.game.FindGameDTO;

public interface GameService {

    void getGameState(String userId, String gameId);

    void findGame(FindGameDTO findGameDTO);

    void checkAnswer(PlayerAnswerDTO playerAnswerDTO);
}

