package com.katiforis.top10.services.impl;

import com.katiforis.top10.DTO.game.*;
import com.katiforis.top10.cache.GameCache;
import com.katiforis.top10.model.Player;
import com.katiforis.top10.repository.*;
import com.katiforis.top10.services.GameService;
import com.katiforis.top10.services.QuestionHandler;
import com.katiforis.top10.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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

@Slf4j
@Service
public class GameServiceImpl implements GameService {

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
	public void findGame(FindGame findGame) {
		log.debug("Start GameServiceImpl.findGame");
		ResponseEntity<Game> response;

		String userId = findGame.getFromUserID();

		Player player = playerRepository.findByPlayerId(userId);


		if(playerQueue.contains(player)){
			return;
		}

		GameState gameStateDTO = gameCache.getGame(findGame.getGameId());

		if(gameStateDTO != null){
			Start startDTO = new Start();
			startDTO.setGameId(String.valueOf(findGame.getGameId()));
			response = new ResponseEntity<>(startDTO, HttpStatus.OK);
			simpMessagingTemplate.convertAndSendToUser(String.valueOf(userId), Constants.MAIN_TOPIC, response);
			return;

		}else{
			if(!playerQueue.isEmpty()){//create new
				Player player1 = playerQueue.get(0);
				Player player2  = player;

				ModelMapper modelMapper = new ModelMapper();
				modelMapper.getConfiguration().setAmbiguityIgnored(true);
				GamePlayer gamePlayer = 	modelMapper.map(player1, GamePlayer.class);
				GamePlayer gamePlayer2 = 	modelMapper.map(player2, GamePlayer.class);
				List<GamePlayer> players = new ArrayList<>();
				players.add(gamePlayer);
				players.add(gamePlayer2);

				GameState newGame = createNewGame(players);
				Start startDTO = new Start();
				startDTO.setGameId(String.valueOf(newGame.getGameId()));
				response = new ResponseEntity<>(startDTO, HttpStatus.OK);
				simpMessagingTemplate.convertAndSendToUser(String.valueOf(userId), Constants.MAIN_TOPIC, response);
				simpMessagingTemplate.convertAndSendToUser(String.valueOf(player1.getPlayerId()), Constants.MAIN_TOPIC, response);
				playerQueue.clear();
			}else{

				if(!playerQueue.contains(userId)){
					playerQueue.add(player);
				}

			}
		}
		log.debug("End GameServiceImpl.findGame");
}

	@Override
	public void checkAnswer(PlayerAnswer playerAnswerDTO) {
		log.debug("Start GameServiceImpl.checkAnswer");
		ModelMapper modelMapper = new ModelMapper();

		GameState gameStateDTO = gameCache.getGame(playerAnswerDTO.getGameId());

		if(gameStateDTO == null){
			return;
		}

		com.katiforis.top10.model.Answer correctAnswer = questionHandler.isAnswerValid(playerAnswerDTO);

		if(correctAnswer != null){
			Answer answer = 	modelMapper.map(correctAnswer, Answer.class);

			if(answer.getDisplayDescription() == null || answer.getDisplayDescription().isEmpty()){
				playerAnswerDTO.setDescription(answer.getDescription());
			}else {
				playerAnswerDTO.setDescription(answer.getDisplayDescription());
			}

			playerAnswerDTO.setPoints(answer.getPoints());
			boolean isDuplicateAnswer = gameCache.addAnswer(playerAnswerDTO);

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
			gameCache.addAnswer(playerAnswerDTO);
		}

		ResponseEntity<Game> response = new ResponseEntity<>(playerAnswerDTO, HttpStatus.OK);
		simpMessagingTemplate.convertAndSend(Constants.GAME_GROUP_TOPIC + gameStateDTO.getGameId(), response);
		log.debug("End GameServiceImpl.checkAnswer");

	}

	private GameState createNewGame(List<GamePlayer> players) {
		log.debug("Start GameServiceImpl.createNewGame");

			GameState gameStateDTO = new GameState();
			gameStateDTO.setGameId(String.valueOf(ThreadLocalRandom.current().nextInt(0, 1000000)));
		ModelMapper modelMapper = new ModelMapper();

		    List<Question> question = modelMapper.map(questionHandler.getQuestions(), new TypeToken<List<Question>>() {}.getType());

			gameStateDTO.setQuestions(question);
			gameStateDTO.setPlayers(players);

			Date now = new Date();
			gameStateDTO.setCurrentDate(now);
			gameStateDTO.setDateStarted(now);

			gameCache.addGame(gameStateDTO);


			Runnable endGame  = () -> endGame(gameStateDTO.getGameId());
			ScheduledExecutorService executorEndGame = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
			executorEndGame.schedule(endGame, 60 * 5 , TimeUnit.SECONDS);

//			Runnable sendCurrectTime  = () -> sendCurrentTime(newGame.getId());
//			ScheduledExecutorService executorSendCurrectTime = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
//			executorSendCurrectTime.scheduleAtFixedRate(sendCurrectTime, 5, 5, TimeUnit.SECONDS);

			//Schedule a task that will be first run in 120 sec and each 120sec
			//If an exception occurs then it's task executions are canceled.
			//executor.scheduleAtFixedRate(task, 60, 60, TimeUnit.SECONDS);

		log.debug("End GameServiceImpl.createNewGame");
		return gameStateDTO;
	}

	@Override
	public void getGameState(String userId, String gameId){
		log.debug("Start GameServiceImpl.getGameState");
		gameId = gameId.replace("\n", "").replace("\r", "");
		userId = userId.replace("\n", "").replace("\r", "");

		GameState gameStateDTO = gameCache.getGame(gameId);
		Date now = new Date();
		gameStateDTO.setCurrentDate(now);

		ResponseEntity<Game> response = new ResponseEntity<>(gameStateDTO, HttpStatus.OK);
		simpMessagingTemplate.convertAndSendToUser(userId, Constants.MAIN_TOPIC, response);

		log.debug("End GameServiceImpl.getGameState");

	}

	void endGame(String gameId){
		log.debug("Start GameServiceImpl.endGame");

		End endDTO = new End(gameId);
		ResponseEntity<Game> response = new ResponseEntity<>(endDTO, HttpStatus.OK);
		simpMessagingTemplate.convertAndSend(Constants.GAME_GROUP_TOPIC + gameId, response);
		gameCache.removeGame(gameId);

		log.debug("End GameServiceImpl.endGame");
	}
}
