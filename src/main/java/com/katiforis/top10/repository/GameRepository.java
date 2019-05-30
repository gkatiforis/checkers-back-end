package com.katiforis.top10.repository;

import com.katiforis.top10.DTO.UserDto;
import com.katiforis.top10.DTO.PlayerAnswer;
import com.katiforis.top10.DTO.response.GameState;
import com.katiforis.top10.DTO.Question;
import com.katiforis.top10.cache.GenericCacheManager;
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
public class GameRepository extends GenericCacheManager<String, GameState> {

    public boolean addAnswer(PlayerAnswer playerAnswerDTO){
        log.debug("Start GameCache.addAnswer");
        synchronized (this.getCache(playerAnswerDTO.getGameId())) {
            GameState gameStateDTO = getGame(playerAnswerDTO.getGameId());

            Question question = gameStateDTO.getQuestions()
                    .stream().filter(questionDTO -> questionDTO.getId() == playerAnswerDTO.getQuestionId())
                    .findFirst().get();

          Set<PlayerAnswer> playerAnswerDTOS =  question.getCurrentAnswers();
            if(playerAnswerDTOS == null){
              playerAnswerDTOS = new HashSet<>();
            }

            UserDto playerDto = gameStateDTO.getPlayers().stream()
                    .filter(p -> p.getUserId().equals(playerAnswerDTO.getUserId()))
                    .findFirst().get();
            playerDto.getPlayerDetails().setEloExtra(playerDto.getPlayerDetails().getEloExtra() + playerAnswerDTO.getPoints());
            playerAnswerDTO.setPlayer(playerDto);

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
