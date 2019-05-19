package com.katiforis.top10.service.impl;

import com.katiforis.top10.DTO.*;
import com.katiforis.top10.DTO.request.FindGame;
import com.katiforis.top10.DTO.response.GameResponse;
import com.katiforis.top10.DTO.response.GameState;
import com.katiforis.top10.DTO.response.GameStats;
import com.katiforis.top10.DTO.response.Start;
import com.katiforis.top10.repository.GameRepository;
import com.katiforis.top10.model.Player;
import com.katiforis.top10.model.PlayerDetails;
import com.katiforis.top10.repository.PlayerRepository;
import com.katiforis.top10.service.GameHandlerService;
import com.katiforis.top10.service.QuestionService;
import com.katiforis.top10.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class GameHandlerServiceImpl implements GameHandlerService {

	private static List<Player> playerQueue = new ArrayList();

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public void findGame(FindGame findGame) {
		log.debug("Start GameHandlerServiceImpl.findGame");
		ResponseEntity<GameResponse> response;

		String userId = findGame.getPlayerId();

		Player player = playerRepository.findByPlayerId(userId);


		if (playerQueue.contains(player)) {
			return;
		}

		GameState gameStateDTO = gameRepository.getGame(findGame.getGameId());

		if (gameStateDTO != null) {
			Start startDTO = new Start(String.valueOf(findGame.getGameId()));
			response = new ResponseEntity<>(startDTO, HttpStatus.OK);
			simpMessagingTemplate.convertAndSendToUser(String.valueOf(userId), Constants.MAIN_TOPIC, response);
			return;

		} else {
			if (!playerQueue.isEmpty()) {//create new
				Player player1 = playerQueue.get(0);
				Player player2 = player;

				modelMapper.getConfiguration().setAmbiguityIgnored(true);
				PlayerDto gamePlayerDto = modelMapper.map(player1, PlayerDto.class);
				PlayerDto gamePlayerDto2 = modelMapper.map(player2, PlayerDto.class);
				List<PlayerDto> playerDtos = new ArrayList<>();
				playerDtos.add(gamePlayerDto);
				playerDtos.add(gamePlayerDto2);

				GameState newGame = createNewGame(playerDtos);
				Start startDTO = new Start(String.valueOf(newGame.getGameId()));
				response = new ResponseEntity<>(startDTO, HttpStatus.OK);
				simpMessagingTemplate.convertAndSendToUser(String.valueOf(userId), Constants.MAIN_TOPIC, response);
				simpMessagingTemplate.convertAndSendToUser(String.valueOf(player1.getPlayerId()), Constants.MAIN_TOPIC, response);
				playerQueue.clear();
			} else {

				if (!playerQueue.contains(userId)) {
					playerQueue.add(player);
				}

			}
		}
		log.debug("End GameHandlerServiceImpl.findGame");
	}

	public GameState createNewGame(List<PlayerDto> playerDtos) {
		log.debug("Start GameHandlerServiceImpl.createNewGame");

		GameState gameStateDTO = new GameState(String.valueOf(ThreadLocalRandom.current().nextInt(0, 1000000)));
		ModelMapper modelMapper = new ModelMapper();

		List<Question> question = modelMapper.map(questionService.getQuestions(), new TypeToken<List<Question>>() {}.getType());

		gameStateDTO.setQuestions(question);
		gameStateDTO.setPlayers(playerDtos);

		Date now = new Date();
		gameStateDTO.setCurrentDate(now);
		gameStateDTO.setDateStarted(now);

		gameRepository.addGame(gameStateDTO);


		Runnable endGame  = () -> endGame(gameStateDTO.getGameId());
		ScheduledExecutorService executorEndGame = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
		executorEndGame.schedule(endGame,  60 * 5 , TimeUnit.SECONDS);

//			Runnable sendCurrectTime  = () -> sendCurrentTime(newGame.getId());
//			ScheduledExecutorService executorSendCurrectTime = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
//			executorSendCurrectTime.scheduleAtFixedRate(sendCurrectTime, 5, 5, TimeUnit.SECONDS);

		//Schedule a task that will be first run in 120 sec and each 120sec
		//If an exception occurs then it's task executions are canceled.
		//executor.scheduleAtFixedRate(task, 60, 60, TimeUnit.SECONDS);

		log.debug("End GameHandlerServiceImpl.createNewGame");
		return gameStateDTO;
	}

	public void endGame(String gameId){
		log.debug("Start GameHandlerServiceImpl.endGame");

		GameState gameState = gameRepository.getGame(gameId);

		GameStats gameStats = new GameStats(gameId);

		List<PlayerDto> playerDtos = gameState.getPlayers();

		//save data
		for(PlayerDto playerDto:playerDtos){
			Player player = playerRepository.findByPlayerId(playerDto.getPlayerId());
			PlayerDetails playerDetails = player.getPlayerDetails();
			PlayerDetailsDto playerDetailsDto = playerDto.getPlayerDetails();
			playerDetails.setElo(playerDetails.getElo() + playerDetailsDto.getEloExtra());
			playerRepository.save(player);
		}

		playerDtos.sort((p1, p2)->
				-1 * Integer.compare(p1.getPlayerDetails().getEloExtra(), p2.getPlayerDetails().getEloExtra()));
		gameStats.setPlayers(playerDtos);

		gameRepository.removeGame(gameId);

		ResponseEntity<GameResponse> response = new ResponseEntity<>(gameStats, HttpStatus.OK);
		simpMessagingTemplate.convertAndSend(Constants.GAME_GROUP_TOPIC + gameId, response);
		log.debug("End GameHandlerServiceImpl.endGame");
	}
}
