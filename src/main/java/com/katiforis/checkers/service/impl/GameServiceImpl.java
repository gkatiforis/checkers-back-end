package com.katiforis.checkers.service.impl;

import com.katiforis.checkers.DTO.*;
import com.katiforis.checkers.DTO.response.GameResponse;
import com.katiforis.checkers.DTO.response.GameState;
import com.katiforis.checkers.repository.GameRepository;
import com.katiforis.checkers.service.GameHandlerService;
import com.katiforis.checkers.service.GameService;
import com.katiforis.checkers.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Slf4j
@Service
public class GameServiceImpl implements GameService {

	@Autowired
	private GameHandlerService gameHandlerService;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Override
	public void checkAnswer(PlayerAnswer playerAnswerDTO) {
		log.debug("Start GameServiceImpl.checkAnswer");
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		playerAnswerDTO.setUserId(principal.getName());

		GameState gameStateDTO = gameRepository.getGame(playerAnswerDTO.getGameId());

		if(gameStateDTO == null){
			return;
		}

		gameStateDTO = gameRepository.addAnswer(playerAnswerDTO);

		gameHandlerService.updateEndGameTime(gameStateDTO.getGameId(), gameStateDTO.getCurrentPlayer().getSecondsRemaining());
		ResponseEntity<GameResponse> response = null;

		if(gameStateDTO.getGameStatus() == GameState.Status.TERMINATED){
			gameHandlerService.endGame(gameStateDTO.getGameId());
		}else{
			playerAnswerDTO.setPlayers(gameStateDTO.getPlayers());
			 response = new ResponseEntity<>(playerAnswerDTO, HttpStatus.OK);
		}
		simpMessagingTemplate.convertAndSend(Constants.GAME_GROUP_TOPIC + gameStateDTO.getGameId(), response);
		log.debug("End GameServiceImpl.checkAnswer");

	}
}
