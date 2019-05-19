package com.katiforis.top10.service.impl;

import com.katiforis.top10.DTO.*;
import com.katiforis.top10.DTO.response.GameResponse;
import com.katiforis.top10.DTO.response.GameState;
import com.katiforis.top10.repository.GameRepository;
import com.katiforis.top10.service.GameService;
import com.katiforis.top10.service.QuestionService;
import com.katiforis.top10.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class GameServiceImpl implements GameService {

	@Autowired
	private QuestionService questionService;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public void checkAnswer(PlayerAnswer playerAnswerDTO) {
		log.debug("Start GameServiceImpl.checkAnswer");

		GameState gameStateDTO = gameRepository.getGame(playerAnswerDTO.getGameId());

		if(gameStateDTO == null){
			return;
		}

		com.katiforis.top10.model.Answer correctAnswer = questionService.isAnswerValid(playerAnswerDTO);

		if(correctAnswer != null){
			Answer answer = 	modelMapper.map(correctAnswer, Answer.class);

			if(answer.getDisplayDescription() == null || answer.getDisplayDescription().isEmpty()){
				playerAnswerDTO.setDescription(answer.getDescription());
			}else {
				playerAnswerDTO.setDescription(answer.getDisplayDescription());
			}

			playerAnswerDTO.setPoints(answer.getPoints());
			boolean isDuplicateAnswer = gameRepository.addAnswer(playerAnswerDTO);

			if(!isDuplicateAnswer){

				if(correctAnswer.getDisplayDescription() == null){
					String[] descriptionArray = correctAnswer.getDescription().split("\\|");
					answer.setDisplayDescription(descriptionArray[0]);
				}
			//	playerAnswerDTO.setAnswer(answerDTO);
				playerAnswerDTO.setPoints(answer.getPoints());
				playerAnswerDTO.setCorrect(true);

			}else{
				if(correctAnswer.getDisplayDescription() == null){
					String[] descriptionArray = correctAnswer.getDescription().split("\\|");
					answer.setDisplayDescription(descriptionArray[0]);
				}

				playerAnswerDTO.setPoints(0);
				playerAnswerDTO.setHasAlreadyBeenSaid(true);
				playerAnswerDTO.setCorrect(false);
			}
		}else{
			playerAnswerDTO.setPoints(0);
			playerAnswerDTO.setCorrect(false);
			playerAnswerDTO.setHasAlreadyBeenSaid(false);
			gameRepository.addAnswer(playerAnswerDTO);
		}

		ResponseEntity<GameResponse> response = new ResponseEntity<>(playerAnswerDTO, HttpStatus.OK);
		simpMessagingTemplate.convertAndSend(Constants.GAME_GROUP_TOPIC + gameStateDTO.getGameId(), response);
		log.debug("End GameServiceImpl.checkAnswer");

	}

	@Override
	public void getGameState(String userId, String gameId){
		log.debug("Start GameServiceImpl.getGameState");
		gameId = gameId.replace("\n", "").replace("\r", "");
		userId = userId.replace("\n", "").replace("\r", "");

		GameState gameStateDTO = gameRepository.getGame(gameId);
		Date now = new Date();
		gameStateDTO.setCurrentDate(now);

		ResponseEntity<GameResponse> response = new ResponseEntity<>(gameStateDTO, HttpStatus.OK);
		simpMessagingTemplate.convertAndSendToUser(userId, Constants.MAIN_TOPIC, response);

		log.debug("End GameServiceImpl.getGameState");

	}
}
