package com.katiforis.top10.services;

import com.katiforis.top10.DTO.game.FindGameDTO;
import com.katiforis.top10.DTO.game.PlayerAnswerDTO;

public interface GameService {

    void getGameState(String userId, String gameId);

    void findGame(FindGameDTO findGameDTO);

    void checkAnswer(PlayerAnswerDTO playerAnswerDTO);
}

