package com.katiforis.top10.config;

import com.katiforis.top10.DTO.game.PlayerAnswerDTO;
import com.katiforis.top10.DTO.game.GameStateDTO;
import com.katiforis.top10.DTO.game.QuestionDTO;
import org.ehcache.Cache;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.builders.ResourcePoolsBuilder.heap;
import static org.ehcache.config.units.MemoryUnit.MB;

@Component
public class GameCache extends GenericCacheManager<String, GameStateDTO> {

    public boolean addAnswer(PlayerAnswerDTO playerAnswerDTO){

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

           return isDuplicateAnswer;
       }
    }


    public void removeGame(String gameId){
        this.removeItem(gameId);
    }

    public void addGame(GameStateDTO gameStateDTO){
       Cache<String, GameStateDTO> cache = cacheManager.createCache(gameStateDTO.getGameId(),
                newCacheConfigurationBuilder(String.class, GameStateDTO.class, heap(10000).offheap(1000, MB)));
        cache.put(gameStateDTO.getGameId(), gameStateDTO);
        caches.put(gameStateDTO.getGameId(), cache);
    }

    public GameStateDTO getGame(String gameId){
        if(gameId == null){
            return null;
        }
        GameStateDTO gameStateDTO =  this.getItem(gameId);
        return gameStateDTO;
    }
}
