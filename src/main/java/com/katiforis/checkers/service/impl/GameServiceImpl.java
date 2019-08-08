package com.katiforis.checkers.service.impl;

import com.katiforis.checkers.DTO.*;
import com.katiforis.checkers.DTO.response.GameResponse;
import com.katiforis.checkers.DTO.response.GameState;
import com.katiforis.checkers.DTO.response.OfferDraw;
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
import java.util.Date;

import static com.katiforis.checkers.util.Constants.OFFER_DRAW_TIME_IN_SECONDS;
import static com.katiforis.checkers.util.Utils.getDiffInSeconds;

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
	public void checkMove(PlayerAnswer playerAnswerDTO) {
		log.debug("Start GameServiceImpl.checkMove");
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
		log.debug("End GameServiceImpl.checkMove");
	}

	@Override
	public void resign(PlayerAnswer playerAnswerDTO) {
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		playerAnswerDTO.setUserId(principal.getName());
		GameState gameState = gameRepository.getGame(playerAnswerDTO.getGameId());
        gameState.setResignUserId(playerAnswerDTO.getUserId());
        gameRepository.updateGame(gameState);
		gameHandlerService.endGame(playerAnswerDTO.getGameId());
	}

	@Override
	public void offerDraw(PlayerAnswer playerAnswerDTO) {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        playerAnswerDTO.setUserId(principal.getName());
        GameState gameState = gameRepository.getGame(playerAnswerDTO.getGameId());

        Date now = new Date();

        if(gameState.getOfferDrawUserId() != null &&
                !gameState.getOfferDrawUserId().equals(playerAnswerDTO.getUserId()) &&
                getDiffInSeconds(gameState.getOfferDrawDate(), now) <= OFFER_DRAW_TIME_IN_SECONDS){
            gameState.setDraw(true);
            gameRepository.updateGame(gameState);
            gameHandlerService.endGame(playerAnswerDTO.getGameId());
        }else{
            gameState.setOfferDrawDate(now);
            gameState.setOfferDrawUserId(playerAnswerDTO.getUserId());
            gameRepository.updateGame(gameState);
            OfferDraw offerDraw = new OfferDraw(playerAnswerDTO.getGameId());
            offerDraw.setByUser(playerAnswerDTO.getUserId());
            ResponseEntity<GameResponse> response = new ResponseEntity<>(offerDraw, HttpStatus.OK);
            simpMessagingTemplate.convertAndSend(Constants.GAME_GROUP_TOPIC + playerAnswerDTO.getGameId(), response);
        }
	}
}
