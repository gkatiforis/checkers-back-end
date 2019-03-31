package com.katiforis.top10.services.impl;

import com.katiforis.top10.DTO.game.*;

import com.katiforis.top10.config.GameCache;
import com.katiforis.top10.model.*;
import com.katiforis.top10.repository.*;
import com.katiforis.top10.services.GameService;
import com.katiforis.top10.services.QuestionHandler;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


@Service
public class GameServiceImpl implements GameService {

	private static final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

	private static List<Player> playerQueue = new ArrayList();

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private QuestionHandler questionHandler;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private GameCache gameCache;

	@Override
	public void findGame(FindGameDTO findGameDTO) {
		logger.debug("Start PlayerController.login");
		ResponseEntity<GameDTO> response;

		String userId = findGameDTO.getFromUserID();

		Player player = playerRepository.findByPlayerId(userId);


		if(playerQueue.contains(player)){
			return;
		}

		GameStateDTO gameStateDTO = gameCache.getGameState(findGameDTO.getGameId());

		if(gameStateDTO != null){
			StartDTO startDTO = new StartDTO();
			startDTO.setGameId(String.valueOf(findGameDTO.getGameId()));
			response = new ResponseEntity<>(startDTO, HttpStatus.OK);
			simpMessagingTemplate.convertAndSendToUser(String.valueOf(userId), "/msg", response);
			return;

		}else{
			if(!playerQueue.isEmpty()){//create new
				Player player1 = playerQueue.get(0);
				Player player2  = player;

				ModelMapper modelMapper = new ModelMapper();
				modelMapper.getConfiguration().setAmbiguityIgnored(true);
				GamePlayerDTO gamePlayerDTO = 	modelMapper.map(player1, GamePlayerDTO.class);
				GamePlayerDTO gamePlayerDTO2 = 	modelMapper.map(player2, GamePlayerDTO.class);
				List<GamePlayerDTO> players = new ArrayList<>();
				players.add(gamePlayerDTO);
				players.add(gamePlayerDTO2);

				GameStateDTO newGame = createNewGame(players);
				StartDTO startDTO = new StartDTO();
				startDTO.setGameId(String.valueOf(newGame.getGameId()));
				response = new ResponseEntity<>(startDTO, HttpStatus.OK);
				simpMessagingTemplate.convertAndSendToUser(String.valueOf(userId), "/msg", response);
				simpMessagingTemplate.convertAndSendToUser(String.valueOf(player1.getPlayerId()), "/msg", response);
				playerQueue.clear();
			}else{

				if(!playerQueue.contains(userId)){
					playerQueue.add(player);
				}

			}
		}




		logger.debug("Start PlayerController.login");
}

	@Override
	public void checkAnswer(PlayerAnswerDTO playerAnswerDTO) {
		logger.debug("Start PlayerController.login");
		ModelMapper modelMapper = new ModelMapper();

		GameStateDTO gameStateDTO = gameCache.getGameState(playerAnswerDTO.getGameId());

		if(gameStateDTO == null){
			return;
		}

		Answer correctAnswer = questionHandler.isAnswerValid(playerAnswerDTO);

		if(correctAnswer != null){
			AnswerDTO answerDTO = 	modelMapper.map(correctAnswer, AnswerDTO.class);

			if(answerDTO.getDisplayDescription() == null || answerDTO.getDisplayDescription().isEmpty()){
				playerAnswerDTO.setDescription(answerDTO.getDescription());
			}else {
				playerAnswerDTO.setDescription(answerDTO.getDisplayDescription());
			}

			playerAnswerDTO.setPoints(answerDTO.getPoints());
			boolean isDuplicateAnswer = gameCache.addAnswer(playerAnswerDTO);

			if(!isDuplicateAnswer){

				if(correctAnswer.getDisplayDescription() == null){
					String[] descriptionArray = correctAnswer.getDescription().split("\\|");
					answerDTO.setDisplayDescription(descriptionArray[0]);
				}
			//	playerAnswerDTO.setAnswer(answerDTO);
				playerAnswerDTO.setPoints(answerDTO.getPoints());
				playerAnswerDTO.setCorrect(true);

			}else{
				if(correctAnswer.getDisplayDescription() == null){
					String[] descriptionArray = correctAnswer.getDescription().split("\\|");
					answerDTO.setDisplayDescription(descriptionArray[0]);
				}

				playerAnswerDTO.setPoints(0);
				playerAnswerDTO.setHasAlreadyBeenSaid(true);
				playerAnswerDTO.setCorrect(false);
			}
		}else{
			playerAnswerDTO.setPoints(0);
			playerAnswerDTO.setCorrect(false);
			playerAnswerDTO.setHasAlreadyBeenSaid(false);
			gameCache.addAnswer(playerAnswerDTO);
		}

		ResponseEntity<GameDTO> response = new ResponseEntity<>(playerAnswerDTO, HttpStatus.OK);
		simpMessagingTemplate.convertAndSend("/g/" + gameStateDTO.getGameId(), response);
		logger.debug("Start PlayerController.login");

	}

	private GameStateDTO createNewGame(List<GamePlayerDTO> players) {
		logger.debug("Start PlayerController.login");

			GameStateDTO gameStateDTO = new GameStateDTO();
			gameStateDTO.setGameId(String.valueOf(ThreadLocalRandom.current().nextInt(0, 1000000)));
		ModelMapper modelMapper = new ModelMapper();

		    List<QuestionDTO> questionDTO = modelMapper.map(questionHandler.getQuestions(), new TypeToken<List<QuestionDTO>>() {}.getType());

			gameStateDTO.setQuestions(questionDTO);
			gameStateDTO.setPlayers(players);

			Date now = new Date();
			gameStateDTO.setCurrentDate(now);
			gameStateDTO.setDateStarted(now);

			gameCache.addGameState(gameStateDTO);


			Runnable endGame  = () -> endGame(gameStateDTO.getGameId());
			ScheduledExecutorService executorEndGame = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
			executorEndGame.schedule(endGame, 60 * 5 , TimeUnit.SECONDS);

//			Runnable sendCurrectTime  = () -> sendCurrentTime(newGame.getId());
//			ScheduledExecutorService executorSendCurrectTime = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
//			executorSendCurrectTime.scheduleAtFixedRate(sendCurrectTime, 5, 5, TimeUnit.SECONDS);

			//Schedule a task that will be first run in 120 sec and each 120sec
			//If an exception occurs then it's task executions are canceled.
			//executor.scheduleAtFixedRate(task, 60, 60, TimeUnit.SECONDS);

		logger.debug("Start PlayerController.login");
			return gameStateDTO;
	}

	@Override
	public void getGameState(String userId, String gameId){
		logger.debug("Start PlayerController.login");
		gameId = gameId.replace("\n", "").replace("\r", "");
		userId = userId.replace("\n", "").replace("\r", "");

		GameStateDTO gameStateDTO = gameCache.getGameState(gameId);
		Date now = new Date();
		gameStateDTO.setCurrentDate(now);

		ResponseEntity<GameDTO> response = new ResponseEntity<>(gameStateDTO, HttpStatus.OK);
		simpMessagingTemplate.convertAndSendToUser(userId, "/msg", response);

		logger.debug("Start PlayerController.login");

	}

	void endGame(String gameId){
		logger.debug("Start PlayerController.login");

		EndDTO endDTO = new EndDTO(gameId);
		ResponseEntity<GameDTO> response = new ResponseEntity<>(endDTO, HttpStatus.OK);
		simpMessagingTemplate.convertAndSend("/g/" + gameId, response);
		gameCache.removeGameState(gameId);

		logger.debug("Start PlayerController.login");
	}

//	void sendCurrentTime(String gameId){
//		CurrentTimeDTO currentTimeDTO = new CurrentTimeDTO();
//		currentTimeDTO.setCurrentDate(Calendar.getInstance().getTime());
//
//		GameDTO messageWrapper = new GameDTO("currentTime", currentTimeDTO);
//		ResponseEntity<GameDTO> response = new ResponseEntity<>(	messageWrapper, HttpStatus.OK);
//		simpMessagingTemplate.convertAndSend("/g/" + gameId, response);
//	}



//	private GameStateDTO mapActiveGameToGameState(ActiveGameDTO activeGame){
//		logger.debug("Start PlayerController.login");
//		GameStateDTO gameStateDTO = new GameStateDTO();
//
//
//
//		List<GamePlayerDTO> gamePlayerDTOS = new ArrayList<>();
//
//		for(PlayerDTO playerDTO:activeGame.getPlayers()){
//
//			//TODO fetch points dynamically
//			gamePlayerDTOS.add(		new GamePlayerDTO(playerDTO.getPlayerId(), playerDTO.getUsername(),  12));
//		}
//
//
//
//		gameStateDTO.setPlayers(gamePlayerDTOS);
//		gameStateDTO.setCurrentAnswers(activeGame.getActiveGameAnswers());
//		gameStateDTO.setQuestions(activeGame.getQuestions());
//
//
//		Calendar cal = Calendar.getInstance();
//		Date now = cal.getTime();
//
//		//cal.add(Calendar.SECOND, -0);
//		Date started = activeGame.getDate_started();
//
//		gameStateDTO.setCurrentDate(now);
//		gameStateDTO.setDateStarted(started);
//		logger.debug("Start PlayerController.login");
//		return gameStateDTO;
//	}
}
