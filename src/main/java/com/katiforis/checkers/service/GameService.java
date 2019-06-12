package com.katiforis.checkers.service;

import com.katiforis.checkers.DTO.PlayerAnswer;

public interface GameService {

    void getGameState(String gameId);

    void checkAnswer(PlayerAnswer playerAnswerDTO);
}

