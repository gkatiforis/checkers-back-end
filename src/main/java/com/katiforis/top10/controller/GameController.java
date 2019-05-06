package com.katiforis.top10.controller;

import com.katiforis.top10.DTO.PlayerAnswer;
import com.katiforis.top10.services.GameService;
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
	public void getGameState(String userId) {
		log.debug("Start GameController.getGameState");
		String [] ids = userId.split("\\|");
			gameService.getGameState(ids[0], ids[1]);
		log.debug("End GameController.getGameState");
	}

	@MessageMapping("/chat/help/getLetter")
	public void getLetter(String userId) {
		log.debug("Start GameController.getLetter");
		String [] ids = userId.split("\\|");
			gameService.getGameState(ids[0], ids[1]);
		log.debug("End GameController.getLetter");
	}
}
