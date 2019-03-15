package com.katiforis.top10.config;

import com.katiforis.top10.DTO.game.ActiveGameAnswerDTO;
import com.katiforis.top10.DTO.game.GameStateDTO;
import com.katiforis.top10.DTO.game.PlayerAnswerDTO;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class GameCache extends GenericCacheManager<String, GameStateDTO> {

    public boolean addAnswer(PlayerAnswerDTO playerAnswerDTO, ActiveGameAnswerDTO activeGameAnswerDTO){

        synchronized (cache){
            GameStateDTO gameStateDTO =  getGameState(playerAnswerDTO.getGameId());
            Set<ActiveGameAnswerDTO> activeGameAnswerDTOS = gameStateDTO.getCurrentAnswers();

            if(activeGameAnswerDTOS == null){
              activeGameAnswerDTOS = new HashSet<>();
            }

            gameStateDTO.getPlayers()
                    .stream()
                    .filter(player -> player.getPlayerId().equals(playerAnswerDTO.getUserId()))
                    .forEach(player -> {
                        player.setPoints(player.getPoints() + activeGameAnswerDTO.getAnswer().getPoints());
                        activeGameAnswerDTO.setPlayer(player);
                    });

            boolean isDuplicateAnswer = !activeGameAnswerDTOS.add(activeGameAnswerDTO);

            if(!isDuplicateAnswer){
                gameStateDTO.setCurrentAnswers(activeGameAnswerDTOS);
                this.saveItem(playerAnswerDTO.getGameId(), gameStateDTO);
            }

           return isDuplicateAnswer;
       }
    }


    public void removeGameState(String gameId){
        this.removeItem(gameId);
    }

    public void addGameState(GameStateDTO gameStateDTO){
        this.saveItem(gameStateDTO.getGameId(), gameStateDTO);
    }

    public GameStateDTO getGameState(String gameId){
        if(gameId == null){
            return null;
        }
        GameStateDTO gameStateDTO =  this.getItem(gameId);
        return gameStateDTO;
    }
}
