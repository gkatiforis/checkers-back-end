package com.katiforis.checkers.controller;

import com.katiforis.checkers.DTO.PlayerAnswer;
import com.katiforis.checkers.service.GameHandlerService;
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

	@Autowired
	GameHandlerService gameHandlerService;

	@MessageMapping("/group/game/{gameId}")
	public void gameTopic(@DestinationVariable String gameId, PlayerAnswer playerAnswerDTO) {
		log.debug("Start GameController.gameTopic");
			playerAnswerDTO.setGameId(gameId);

			if( playerAnswerDTO.getResign()){
				gameService.resign(playerAnswerDTO);
			}else if( playerAnswerDTO.getOfferDraw()){
				gameService.offerDraw(playerAnswerDTO);
			}else{
				gameService.checkMove(playerAnswerDTO);
			}
		log.debug("End GameController.gameTopic");
	}

	@MessageMapping("/gamestate")
	public void getGameState(String gameId) {
		log.debug("Start GameController.getGameState");
		gameHandlerService.getGameState(gameId);
		log.debug("End GameController.getGameState");
	}
}
