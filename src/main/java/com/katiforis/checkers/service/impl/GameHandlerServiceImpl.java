package com.katiforis.checkers.service.impl;

import com.katiforis.checkers.DTO.*;
import com.katiforis.checkers.game.Board;
import com.katiforis.checkers.game.Piece;
import com.katiforis.checkers.DTO.request.FindGame;
import com.katiforis.checkers.DTO.response.GameResponse;
import com.katiforis.checkers.DTO.response.GameState;
import com.katiforis.checkers.DTO.response.GameStats;
import com.katiforis.checkers.DTO.response.Start;
import com.katiforis.checkers.repository.GameRepository;
import com.katiforis.checkers.model.User;
import com.katiforis.checkers.model.PlayerDetails;
import com.katiforis.checkers.repository.UserRepository;
import com.katiforis.checkers.service.GameHandlerService;
import com.katiforis.checkers.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
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

	private static List<User> userQueue = new ArrayList();

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public void findGame(FindGame findGame) {
		log.debug("Start GameHandlerServiceImpl.findGame");
		ResponseEntity<GameResponse> response;

        Principal principal = SecurityContextHolder.getContext().getAuthentication();
		String userId = principal.getName();

		User user = userRepository.findByUserId(userId);


		if (userQueue.contains(user)) {
			return;
		}

		GameState gameStateDTO = gameRepository.getGame(findGame.getGameId());

		if (gameStateDTO != null) {
			Start startDTO = new Start(String.valueOf(findGame.getGameId()));
			response = new ResponseEntity<>(startDTO, HttpStatus.OK);
			simpMessagingTemplate.convertAndSendToUser(String.valueOf(userId), Constants.MAIN_TOPIC, response);
			return;

		} else {
			if (!userQueue.isEmpty()) {//create new
				User user1 = userQueue.get(0);
				User user2 = user;

				modelMapper.getConfiguration().setAmbiguityIgnored(true);
				UserDto gameUserDto = modelMapper.map(user1, UserDto.class);
				UserDto gameUserDto2 = modelMapper.map(user2, UserDto.class);
				List<UserDto> playerDtos = new ArrayList<>();

				gameUserDto.setColor(Piece.DARK);
				gameUserDto2.setColor(Piece.LIGHT);
				playerDtos.add(gameUserDto);
				playerDtos.add(gameUserDto2);

				GameState newGame = createNewGame(playerDtos);
				Start startDTO = new Start(String.valueOf(newGame.getGameId()));
				response = new ResponseEntity<>(startDTO, HttpStatus.OK);
				simpMessagingTemplate.convertAndSendToUser(String.valueOf(userId), Constants.MAIN_TOPIC, response);
				simpMessagingTemplate.convertAndSendToUser(String.valueOf(user1.getUserId()), Constants.MAIN_TOPIC, response);
				userQueue.clear();
			} else {

				if (!userQueue.contains(userId)) {
					userQueue.add(user);
				}

			}
		}
		log.debug("End GameHandlerServiceImpl.findGame");
	}

	public GameState createNewGame(List<UserDto> playerDtos) {
		log.debug("Start GameHandlerServiceImpl.createNewGame");

		GameState gameStateDTO = new GameState(String.valueOf(ThreadLocalRandom.current().nextInt(0, 1000000)));
		Board board = new Board();
		board.initialBoardSetup();
		gameStateDTO.setBoard(board);
		gameStateDTO.setPlayers(playerDtos);
		gameStateDTO.setCurrentPlayer(playerDtos.get(1));
		Date now = new Date();
		gameStateDTO.setCurrentDate(now);
		gameStateDTO.setDateStarted(now);

		gameRepository.addGame(gameStateDTO);


		Runnable endGame  = () -> endGame(gameStateDTO.getGameId());
		ScheduledExecutorService executorEndGame = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
		executorEndGame.schedule(endGame,  60 * 605 , TimeUnit.SECONDS);

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

		List<UserDto> playerDtos = gameState.getPlayers();

		//save data
		for(UserDto playerDto:playerDtos){
			User user = userRepository.findByUserId(playerDto.getUserId());
			PlayerDetails playerDetails = user.getPlayerDetails();
			playerDetails.setElo(playerDetails.getElo() + 10);
			userRepository.save(user);
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
