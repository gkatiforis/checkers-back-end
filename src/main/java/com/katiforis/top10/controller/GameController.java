package com.katiforis.top10.controller;


import com.katiforis.top10.DTO.game.PlayerAnswerDTO;
import com.katiforis.top10.DTO.game.FindGameDTO;
import com.katiforis.top10.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/game")
public class GameController {

	@Autowired
	GameService gameService;

	@MessageMapping("/group/word/{gameId}")
	public void checkAnswer(@DestinationVariable String gameId, PlayerAnswerDTO playerAnswerDTO) {
		playerAnswerDTO.setGameId(gameId);
		gameService.checkAnswer(playerAnswerDTO);
	}

	@MessageMapping("/chat/find")
	public void findGame(FindGameDTO findGameDTO) {
	      gameService.findGame(findGameDTO);

	}

	@MessageMapping("/chat/gamestate")
	public void getGameState(String userId) {
		String [] ids = userId.split("\\|");
			gameService.getGameState(ids[0], ids[1]);

	}

	@MessageMapping("/chat/help/getLetter")
	public void getLetter(String userId) {
		String [] ids = userId.split("\\|");
		gameService.getGameState(ids[0], ids[1]);

	}
}
