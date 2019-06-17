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

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
@Service
public class GameHandlerServiceImpl implements GameHandlerService {

	private static final int MAX_CONCURRENTLY_GAMES = 10000;
	private static List<User> userQueue = new ArrayList();

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private ModelMapper modelMapper;

	private ScheduledExecutorService scheduleExecutor;
	private Map<String, ScheduledFuture<?>> tasks = new HashMap<>();

	@PostConstruct
	public void init(){
		scheduleExecutor = Executors.newScheduledThreadPool(MAX_CONCURRENTLY_GAMES);
	}

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
				gameUserDto2.setIsCurrent(true);
				gameUserDto.setIsCurrent(false);
				gameUserDto.setSecondsRemaining(10*60l);
				gameUserDto2.setSecondsRemaining(10*60l);
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
		Date now = new Date();
		gameStateDTO.setCurrentDate(now);
		gameStateDTO.setDateStarted(now);
		gameStateDTO.setLastMoveDate(now);
		gameStateDTO.setGameMaxTime(10);
		gameRepository.addGame(gameStateDTO);

		tasks.put(gameStateDTO.getGameId(),
				scheduleExecutor.schedule(() -> endGame(gameStateDTO.getGameId()),
				gameStateDTO.getPlayers().get(0).getSecondsRemaining(),  TimeUnit.SECONDS));

		log.debug("End GameHandlerServiceImpl.createNewGame");
		return gameStateDTO;
	}

	public void updateEndGameTime(String gameId, long timeSeconds){
		ScheduledFuture<?> scheduledFuture = tasks.get(gameId);
		if (scheduledFuture !=  null)
		{
			scheduledFuture.cancel(true);
		}
		scheduledFuture = scheduleExecutor.schedule(() -> endGame(gameId), timeSeconds,  TimeUnit.SECONDS);
		tasks.put(gameId,scheduledFuture);
	}


	public void endGame(String gameId){
		log.debug("Start GameHandlerServiceImpl.endGame");
		ScheduledFuture<?> scheduledFuture = tasks.get(gameId);
		if (scheduledFuture !=  null)
		{
			scheduledFuture.cancel(true);
		}
		tasks.remove(gameId);

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

	@Override
	public void getGameState(String gameId){
		log.debug("Start GameServiceImpl.getGameState");
		Principal principal = SecurityContextHolder.getContext().getAuthentication();

		gameId = gameId.replace("\n", "").replace("\r", "");

		GameState gameStateDTO = gameRepository.getGame(gameId);
		Date now = new Date();
		gameStateDTO.setCurrentDate(now);
		ResponseEntity<GameResponse> response = new ResponseEntity<>(gameStateDTO, HttpStatus.OK);
		simpMessagingTemplate.convertAndSendToUser(principal.getName(), Constants.MAIN_TOPIC, response);

		log.debug("End GameServiceImpl.getGameState");
	}
}
