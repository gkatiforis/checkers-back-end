package com.katiforis.top10.cache;

import com.katiforis.top10.DTO.game.PlayerAnswerDTO;
import com.katiforis.top10.DTO.game.GameStateDTO;
import com.katiforis.top10.DTO.game.QuestionDTO;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.builders.ResourcePoolsBuilder.heap;
import static org.ehcache.config.units.MemoryUnit.MB;

@Slf4j
@Component
public class GameCache extends GenericCacheManager<String, GameStateDTO> {

    public boolean addAnswer(PlayerAnswerDTO playerAnswerDTO){
        log.debug("Start GameCache.addAnswer");
        synchronized (this.getCache(playerAnswerDTO.getGameId())) {
            GameStateDTO gameStateDTO = getGame(playerAnswerDTO.getGameId());

            QuestionDTO question = gameStateDTO.getQuestions()
                    .stream().filter(questionDTO -> questionDTO.getId() == playerAnswerDTO.getQuestionId())
                    .findFirst().get();

          Set<PlayerAnswerDTO> playerAnswerDTOS =  question.getCurrentAnswers();
            if(playerAnswerDTOS == null){
              playerAnswerDTOS = new HashSet<>();
            }

            gameStateDTO.getPlayers()
                    .stream()
                    .filter(player -> player.getPlayerId().equals(playerAnswerDTO.getUserId()))
                    .forEach(player -> {
                        player.setPoints(player.getPoints() + playerAnswerDTO.getPoints());
                        playerAnswerDTO.setPlayer(player);
                    });

            boolean isDuplicateAnswer = !playerAnswerDTOS.add(playerAnswerDTO);

            if(!isDuplicateAnswer){
                question.setCurrentAnswers(playerAnswerDTOS);
                this.saveItem(playerAnswerDTO.getGameId(), gameStateDTO);
            }

            log.debug("End GameCache.addAnswer");
           return isDuplicateAnswer;
       }
    }


    public void removeGame(String gameId){
        log.debug("Start GameCache.removeGame");
        this.removeItem(gameId);
        log.debug("End GameCache.removeGame");
    }

    public void addGame(GameStateDTO gameStateDTO){
        log.debug("Start GameCache.addGame");
       Cache<String, GameStateDTO> cache = cacheManager.createCache(gameStateDTO.getGameId(),
                newCacheConfigurationBuilder(String.class, GameStateDTO.class, heap(10000).offheap(1000, MB)));
        cache.put(gameStateDTO.getGameId(), gameStateDTO);
        caches.put(gameStateDTO.getGameId(), cache);
        log.debug("End GameCache.addGame");
    }

    public GameStateDTO getGame(String gameId){
        log.debug("Start GameCache.getGame");
        if(gameId == null){
            return null;
        }
        GameStateDTO gameStateDTO = this.getItem(gameId);
        log.debug("End GameCache.getGame");
        return gameStateDTO;
    }
}
