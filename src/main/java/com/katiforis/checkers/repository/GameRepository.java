package com.katiforis.checkers.repository;

import com.katiforis.checkers.DTO.PlayerAnswer;
import com.katiforis.checkers.game.Board;
import com.katiforis.checkers.game.Cell;
import com.katiforis.checkers.game.Move;
import com.katiforis.checkers.DTO.response.GameState;
import com.katiforis.checkers.cache.GenericCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.builders.ResourcePoolsBuilder.heap;
import static org.ehcache.config.units.MemoryUnit.MB;

@Slf4j
@Component
public class GameRepository extends GenericCacheManager<String, GameState> {

    public GameState addAnswer(PlayerAnswer playerAnswerDTO){
        log.debug("Start GameCache.addAnswer");
        synchronized (this.getCache(playerAnswerDTO.getGameId())) {
            GameState gameStateDTO = getGame(playerAnswerDTO.getGameId());


            Move move = playerAnswerDTO.getMove();
            Board board = gameStateDTO.getBoard();
            boolean isCaptureMove = board.isCaptureMove(move.getFrom(), move.getTo());
            List<Cell> cells = board.movePiece(move.getFrom().getX(), move.getFrom().getY(), move.getTo().getX(), move.getTo().getY());

            List<Move> moves = board.possibleMoves(cells);
            if(!isCaptureMove || moves.stream().noneMatch(Move::isObligatoryMove)){
                if(gameStateDTO.getCurrentPlayer().equals(gameStateDTO.getPlayers().get(0))){
                    gameStateDTO.setCurrentPlayer(gameStateDTO.getPlayers().get(1));
                }else{
                    gameStateDTO.setCurrentPlayer(gameStateDTO.getPlayers().get(0));
                }
            }

            if(board.hasGameEnd()){
                gameStateDTO.setGameStatus(GameState.Status.TERMINATED);
            }

            this.saveItem(playerAnswerDTO.getGameId(), gameStateDTO);

            log.debug("End GameCache.addAnswer");
           return gameStateDTO;
       }
    }


    public void removeGame(String gameId){
        log.debug("Start GameCache.removeGame");
        this.removeItem(gameId);
        log.debug("End GameCache.removeGame");
    }

    public void addGame(GameState gameStateDTO){
        log.debug("Start GameCache.addGame");
       Cache<String, GameState> cache = cacheManager.createCache(gameStateDTO.getGameId(),
                newCacheConfigurationBuilder(String.class, GameState.class, heap(10000).offheap(10, MB)));
        cache.put(gameStateDTO.getGameId(), gameStateDTO);
        caches.put(gameStateDTO.getGameId(), cache);
        log.debug("End GameCache.addGame");
    }

    public GameState getGame(String gameId){
        log.debug("Start GameCache.getGame");
        if(gameId == null){
            return null;
        }
        GameState gameStateDTO = this.getItem(gameId);
        log.debug("End GameCache.getGame");
        return gameStateDTO;
    }
}
