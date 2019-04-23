package com.katiforis.top10.services;

import com.katiforis.top10.DTO.game.PlayerAnswer;
import com.katiforis.top10.DTO.game.FindGame;

public interface GameService {

    void getGameState(String userId, String gameId);

    void findGame(FindGame findGame);

    void checkAnswer(PlayerAnswer playerAnswerDTO);
}

