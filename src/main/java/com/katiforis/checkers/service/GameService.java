package com.katiforis.checkers.service;

import com.katiforis.checkers.DTO.PlayerAnswer;

public interface GameService {

    void checkMove(PlayerAnswer playerAnswerDTO);

    void resign(PlayerAnswer playerAnswerDTO);

    void offerDraw(PlayerAnswer playerAnswerDTO);
}

