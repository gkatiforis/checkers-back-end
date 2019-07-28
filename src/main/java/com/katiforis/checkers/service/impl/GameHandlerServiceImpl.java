package com.katiforis.checkers.service.impl;

import com.katiforis.checkers.DTO.GameType;
import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.DTO.request.FindGame;
import com.katiforis.checkers.DTO.response.GameResponse;
import com.katiforis.checkers.DTO.response.GameState;
import com.katiforis.checkers.DTO.response.GameStats;
import com.katiforis.checkers.DTO.response.Start;
import com.katiforis.checkers.exception.GameException;
import com.katiforis.checkers.game.Board;
import com.katiforis.checkers.game.Piece;
import com.katiforis.checkers.model.PlayerDetails;
import com.katiforis.checkers.model.User;
import com.katiforis.checkers.repository.GameRepository;
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
    private Map<String, ScheduledFuture<?>> endGameTasks = new HashMap<>();
    private Map<String, ScheduledFuture<?>> restartGameTasks = new HashMap<>();

    @PostConstruct
    public void init() {
        scheduleExecutor = Executors.newScheduledThreadPool(MAX_CONCURRENTLY_GAMES);
    }

    @Override
    public void findGame(FindGame findGame) throws GameException {
        log.debug("Start GameHandlerServiceImpl.findGame");
        ResponseEntity<GameResponse> response;

        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        String userId = principal.getName();

        User user = userRepository.findByUserId(userId);


        if (userQueue.contains(user)) {
            return;
        }

        GameState gameStateDTO = gameRepository.getGame(findGame.getGameId());

        if (gameStateDTO != null && gameStateDTO.getGameStatus() == GameState.Status.IN_PROGRESS) {
            Start startDTO = new Start(findGame.getGameId());
            response = new ResponseEntity<>(startDTO, HttpStatus.OK);
            simpMessagingTemplate.convertAndSendToUser(userId, Constants.MAIN_TOPIC, response);
            return;
        } else if (gameStateDTO != null &&
                gameStateDTO.getGameStatus() == GameState.Status.PLAYERS_SELECTION &&
                findGame.isRestart()) {
            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            UserDto gameUserDto = modelMapper.map(user, UserDto.class);
            GameState gameState = gameRepository.addPlayer(gameStateDTO.getGameId(), gameUserDto);
            if (gameState.getPlayers().size() == 2) {
                restartGame(gameStateDTO.getGameId());
            }
        } else {

            checkFee(user, findGame.getGameType());

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
                gameUserDto.setSecondsRemaining(10 * 60l);
                gameUserDto2.setSecondsRemaining(10 * 60l);
                playerDtos.add(gameUserDto);
                playerDtos.add(gameUserDto2);
                playerDtos.sort(Comparator.comparing(UserDto::getColor));

                payFee(playerDtos, findGame.getGameType());

                GameState newGame = createNewGame(playerDtos, findGame.getGameType());
                Start startDTO = new Start(String.valueOf(newGame.getGameId()));
                response = new ResponseEntity<>(startDTO, HttpStatus.OK);
                simpMessagingTemplate.convertAndSendToUser(userId, Constants.MAIN_TOPIC, response);
                simpMessagingTemplate.convertAndSendToUser(user1.getUserId(), Constants.MAIN_TOPIC, response);
                userQueue.clear();
            } else {
                userQueue.add(user);
            }
        }
        log.debug("End GameHandlerServiceImpl.findGame");
    }

    public GameState createNewGame(List<UserDto> playerDtos, GameType gameType) {
        log.debug("Start GameHandlerServiceImpl.createNewGame");

        GameState gameStateDTO = new GameState(String.valueOf(ThreadLocalRandom.current().nextInt(0, 1000000)));
        Board board = new Board();
        board.initialBoardSetup();
        gameStateDTO.setBoard(board);
        gameStateDTO.setPlayers(playerDtos);
        gameStateDTO.setGameType(gameType);
        Date now = new Date();
        gameStateDTO.setCurrentDate(now);
        gameStateDTO.setDateStarted(now);
        gameStateDTO.setLastMoveDate(now);
        gameStateDTO.setGameMaxTime(10);
        gameStateDTO.setGameStatus(GameState.Status.IN_PROGRESS);
        gameRepository.addGame(gameStateDTO);

        endGameTasks.put(gameStateDTO.getGameId(),
                scheduleExecutor.schedule(() -> endGame(gameStateDTO.getGameId()),
                        gameStateDTO.getPlayers().get(0).getSecondsRemaining(), TimeUnit.SECONDS));

        log.debug("End GameHandlerServiceImpl.createNewGame");
        return gameStateDTO;
    }

    public void payFee(List<UserDto> playerDtos, GameType gameType) {
        if (gameType.getFee() > 0) {
            for (UserDto u : playerDtos) {
                User user = userRepository.findByUserId(u.getUserId());
                user.getPlayerDetails().setCoins(user.getPlayerDetails().getCoins() - gameType.getFee());
                userRepository.save(user);
            }
        }
    }

    public void checkFee(User user, GameType gameType) throws GameException {
        if (gameType.getFee() > 0) {
            if(user.getPlayerDetails().getCoins() < gameType.getFee()){
                throw new GameException("Not enough fee");
            }
        }
    }

    public void updateEndGameTime(String gameId, long timeSeconds) {
        ScheduledFuture<?> scheduledFuture = endGameTasks.get(gameId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        scheduledFuture = scheduleExecutor.schedule(() -> endGame(gameId), timeSeconds, TimeUnit.SECONDS);
        endGameTasks.put(gameId, scheduledFuture);
    }

    public void endGame(String gameId) {
        log.debug("Start GameHandlerServiceImpl.endGame");
        ScheduledFuture<?> scheduledFuture = endGameTasks.get(gameId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        endGameTasks.remove(gameId);

        GameState gameState = gameRepository.getGame(gameId);

        GameType gameType = gameState.getGameType();
        UserDto userDto = gameState.getPlayers().get(0);
        UserDto userDto2 = gameState.getPlayers().get(1);
        UserDto loser = null;
        UserDto winner = null;

        if (gameState.isDraw()) {
            winner = loser = null;
        } else if (gameState.getResignUserId() != null) {
            if (userDto.getUserId().equals(gameState.getResignUserId())) {
                loser = userDto;
                winner = userDto2;
            } else {
                loser = userDto2;
                winner = userDto;
            }
        } else if (userDto.getIsCurrent()) {
            loser = userDto;
            winner = userDto2;
        } else if (userDto2.getIsCurrent()) {
            loser = userDto2;
            winner = userDto;
        } else if (gameState.getBoard().getLoser() != null) {
            String loserColor = gameState.getBoard().getLoser();
            if (userDto.getColor().equals(loserColor)) {
                loser = userDto;
                winner = userDto2;
            } else {
                loser = userDto2;
                winner = userDto;
            }
        }

        GameStats gameStats = new GameStats(gameId);

        if (loser == null && winner == null) {
            gameStats.setDraw(true);
            gameStats.setPlayers(Arrays.asList(userDto, userDto2));
        } else {
            User loserUser = userRepository.findByUserId(loser.getUserId());
            PlayerDetails playerDetailsLoser = loserUser.getPlayerDetails();
            int ratingLoser = playerDetailsLoser.getElo() + gameType.getEloExtraLose();
            int pointsLoser = playerDetailsLoser.getLevelPoints() + gameType.getPointsExtraLose();
            int coinsLoser = playerDetailsLoser.getCoins() + gameType.getCoinsExtraLoser();
            if (ratingLoser > 0) {
                playerDetailsLoser.setElo(ratingLoser);
            } else {
                playerDetailsLoser.setElo(0);
            }
            if (pointsLoser > 0) {
                playerDetailsLoser.setLevelPoints(pointsLoser);
            } else {
                playerDetailsLoser.setLevelPoints(0);
            }
            playerDetailsLoser.setCoins(coinsLoser);

            userRepository.save(loserUser);
            loser.getPlayerDetails().setElo(playerDetailsLoser.getElo());
            loser.getPlayerDetails().setEloExtra(gameType.getEloExtraLose());
            loser.getPlayerDetails().setCoins(playerDetailsLoser.getCoins());


            User winnerUser = userRepository.findByUserId(winner.getUserId());
            PlayerDetails playerDetailsWinner = winnerUser.getPlayerDetails();
            int ratingWinner = playerDetailsWinner.getElo() + gameType.getEloExtraWin();
            int pointsWinner = playerDetailsWinner.getLevelPoints() + gameType.getPointsExtraWin();
            int coinsWinner = playerDetailsWinner.getCoins() + gameType.getCoinsExtraWin();

            playerDetailsWinner.setElo(ratingWinner);
            playerDetailsWinner.setLevelPoints(pointsWinner);
            playerDetailsWinner.setCoins(coinsWinner);
            int maxExp = playerDetailsWinner.getLevel() * 20;
            if (pointsWinner >= maxExp) {
                playerDetailsWinner.setLevel(playerDetailsWinner.getLevel() + 1);
                playerDetailsWinner.setLevelPoints(0);

            }
            userRepository.save(winnerUser);
            winner.getPlayerDetails().setElo(playerDetailsWinner.getElo());
            winner.getPlayerDetails().setEloExtra(gameType.getEloExtraWin());
            winner.getPlayerDetails().setCoins(playerDetailsWinner.getCoins());
            winner.getPlayerDetails().setCoinsExtra(gameType.getCoinsExtraWin());

            gameStats.setWinnerColor(winner.getColor());
            gameStats.setPlayers(Arrays.asList(winner, loser));
        }

//		playerDtos.sort((p1, p2)->
//				-1 * Integer.compare(p1.getPlayerDetails().getEloExtra(), p2.getPlayerDetails().getEloExtra()));
//		gameStats.setPlayers(playerDtos);

        resetGame(gameId);

        ResponseEntity<GameResponse> response = new ResponseEntity<>(gameStats, HttpStatus.OK);
        simpMessagingTemplate.convertAndSend(Constants.GAME_GROUP_TOPIC + gameId, response);
        log.debug("End GameHandlerServiceImpl.endGame");
    }

    @Override
    public void getGameState(String gameId) {
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

    public void resetGame(String gameId) {
        GameState gameStateDTO = gameRepository.getGame(gameId);

        gameStateDTO.setDraw(false);
        gameStateDTO.setOfferDrawUserId(null);
        gameStateDTO.setResignUserId(null);
        gameStateDTO.setPlayers(new ArrayList<>());
        gameStateDTO.setGameStatus(GameState.Status.PLAYERS_SELECTION);

        gameRepository.updateGame(gameStateDTO);

        restartGameTasks.put(gameId,
                scheduleExecutor.schedule(() -> restartGame(gameId),
                        30, TimeUnit.SECONDS));
    }

    public void restartGame(String gameId) {
        ScheduledFuture<?> scheduledFuture = restartGameTasks.get(gameId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        restartGameTasks.remove(gameId);
        GameState gameStateDTO = gameRepository.getGame(gameId);

        if (gameStateDTO.getPlayers().size() != 2) {
            gameRepository.removeGame(gameId);
            return;
        }

        Board board = new Board();
        board.initialBoardSetup();
        gameStateDTO.setBoard(board);

        UserDto userDto = gameStateDTO.getPlayers().get(0);
        UserDto userDto2 = gameStateDTO.getPlayers().get(1);
        userDto.setColor(Piece.DARK);
        userDto2.setColor(Piece.LIGHT);
        userDto2.setIsCurrent(true);
        userDto.setIsCurrent(false);
        userDto.setSecondsRemaining(10 * 60l);
        userDto2.setSecondsRemaining(10 * 60l);

        Date now = new Date();
        gameStateDTO.setCurrentDate(now);
        gameStateDTO.setDateStarted(now);
        gameStateDTO.setLastMoveDate(now);
        gameStateDTO.setGameMaxTime(10);
        gameStateDTO.setGameStatus(GameState.Status.IN_PROGRESS);
        gameStateDTO.getPlayers().sort(Comparator.comparing(UserDto::getColor));
        gameRepository.updateGame(gameStateDTO);

        gameRepository.startGame(gameId);

        endGameTasks.put(gameStateDTO.getGameId(),
                scheduleExecutor.schedule(() -> endGame(gameStateDTO.getGameId()),
                        gameStateDTO.getPlayers().get(0).getSecondsRemaining(), TimeUnit.SECONDS));

        for (UserDto user : gameStateDTO.getPlayers()) {
            Start startDTO = new Start(gameId);
            ResponseEntity<GameResponse> response = new ResponseEntity<>(startDTO, HttpStatus.OK);
            simpMessagingTemplate.convertAndSendToUser(user.getUserId(), Constants.MAIN_TOPIC, response);
        }

    }
}
