package com.katiforis.top10.service;

import com.katiforis.top10.DTO.PlayerAnswer;

public interface GameService {

    void getGameState(String userId, String gameId);

    void checkAnswer(PlayerAnswer playerAnswerDTO);
}

