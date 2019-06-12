package com.katiforis.checkers.controller;

import com.katiforis.checkers.DTO.PlayerAnswer;
import com.katiforis.checkers.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@MessageMapping("/game")
public class GameController {

	@Autowired
	GameService gameService;

	@MessageMapping("/group/word/{gameId}")
	public void checkAnswer(@DestinationVariable String gameId, PlayerAnswer playerAnswerDTO) {
		log.debug("Start GameController.checkAnswer");
			playerAnswerDTO.setGameId(gameId);
			gameService.checkAnswer(playerAnswerDTO);
		log.debug("End GameController.checkAnswer");
	}

	@MessageMapping("/gamestate")
	public void getGameState(String gameId) {
		log.debug("Start GameController.getGameState");
			gameService.getGameState(gameId);
		log.debug("End GameController.getGameState");
	}

	@MessageMapping("/chat/help/getLetter")
	public void getLetter(String gameId) {
		log.debug("Start GameController.getLetter");
			gameService.getGameState(gameId);
		log.debug("End GameController.getLetter");
	}
}
