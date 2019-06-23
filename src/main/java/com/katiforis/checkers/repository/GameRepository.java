package com.katiforis.checkers.repository;

import com.katiforis.checkers.DTO.PlayerAnswer;
import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.game.Board;
import com.katiforis.checkers.game.Cell;
import com.katiforis.checkers.game.Move;
import com.katiforis.checkers.DTO.response.GameState;
import com.katiforis.checkers.cache.CacheRepository;
import com.katiforis.checkers.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.builders.ResourcePoolsBuilder.heap;
import static org.ehcache.config.units.MemoryUnit.MB;

@Slf4j
@Component
public class GameRepository extends CacheRepository<String, GameState> {

    public GameState addAnswer(PlayerAnswer playerAnswerDTO) {
        log.debug("Start GameCache.addAnswer");
        synchronized (this.getCache(playerAnswerDTO.getGameId())) {
            GameState gameStateDTO = getGame(playerAnswerDTO.getGameId());
            Move move = playerAnswerDTO.getMove();
            Board board = gameStateDTO.getBoard();

            if (!board.isValidMove(move.getFrom(), move.getTo())) {
                return gameStateDTO;
            }

            boolean isCaptureMove = board.isCaptureMove(move.getFrom(), move.getTo());
            List<Cell> cells = board.movePiece(move.getFrom().getX(), move.getFrom().getY(), move.getTo().getX(), move.getTo().getY());

            Date now = new Date();

            UserDto currentPlayer = gameStateDTO.getPlayers().stream().filter(p -> p.getIsCurrent()).findFirst().get();
            long remainingTime = currentPlayer.getSecondsRemaining() -
                    Utils.getDiffInSeconds(gameStateDTO.getLastMoveDate(), now);
            currentPlayer.setSecondsRemaining(remainingTime);
            gameStateDTO.setLastMoveDate(now);
            UserDto prevPlayer = currentPlayer;

            List<Move> moves = board.possibleMoves(cells);
            if (!isCaptureMove || moves.stream().noneMatch(Move::isObligatoryMove)) {
                prevPlayer.setIsCurrent(false);
                if (prevPlayer.equals(gameStateDTO.getPlayers().get(0))) {
                    gameStateDTO.getPlayers().set(0, prevPlayer);
                    currentPlayer = gameStateDTO.getPlayers().get(1);
                    currentPlayer.setIsCurrent(true);
                } else {
                    gameStateDTO.getPlayers().set(1, prevPlayer);
                    currentPlayer = gameStateDTO.getPlayers().get(0);
                    currentPlayer.setIsCurrent(true);
                }
            }

            if (board.hasGameEnd()) {
                gameStateDTO.setGameStatus(GameState.Status.TERMINATED);
            }

            this.saveItem(playerAnswerDTO.getGameId(), gameStateDTO);

            log.debug("End GameCache.addAnswer");
            return gameStateDTO;
        }
    }


    public void removeGame(String gameId) {
        log.debug("Start GameCache.removeGame");
        this.removeItem(gameId);
        log.debug("End GameCache.removeGame");
    }

    public void addGame(GameState gameStateDTO) {
        log.debug("Start GameCache.addGame");
        Cache<String, GameState> cache = cacheManager.createCache(gameStateDTO.getGameId(),
                newCacheConfigurationBuilder(String.class, GameState.class, heap(10000).offheap(10, MB)));
        cache.put(gameStateDTO.getGameId(), gameStateDTO);
        caches.put(gameStateDTO.getGameId(), cache);
        log.debug("End GameCache.addGame");
    }

    public GameState getGame(String gameId) {
        log.debug("Start GameCache.getGame");
        if (gameId == null) {
            return null;
        }
        GameState gameStateDTO = this.getItem(gameId);
        log.debug("End GameCache.getGame");
        return gameStateDTO;
    }

    public GameState addPlayer(String gameId, UserDto userDto) {
        log.debug("Start GameCache.addPlayer");
        synchronized (this.getCache(gameId)) {
            GameState gameStateDTO = getGame(gameId);
            if(!gameStateDTO.getPlayers().contains(userDto)){
                gameStateDTO.getPlayers().add(userDto);
                this.saveItem(gameId, gameStateDTO);
            }
            log.debug("End GameCache.addPlayer");
            return gameStateDTO;
        }
    }

    public void startGame(String gameId) {
        log.debug("Start GameCache.startGame");
        synchronized (this.getCache(gameId)) {
            GameState gameStateDTO = getGame(gameId);
            Date now = new Date();
            gameStateDTO.setCurrentDate(now);
            gameStateDTO.setDateStarted(now);
            gameStateDTO.setGameStatus(GameState.Status.IN_PROGRESS);
            this.saveItem(gameId, gameStateDTO);
            log.debug("End GameCache.startGame");
        }
    }

    public void updateGame(GameState gameState) {
        log.debug("Start GameCache.updateGame");
        synchronized (this.getCache(gameState.getGameId())) {
            this.saveItem(gameState.getGameId(), gameState);
            log.debug("End GameCache.updateGame");
        }
    }
}

