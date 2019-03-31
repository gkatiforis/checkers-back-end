package com.katiforis.top10.config;

import com.katiforis.top10.DTO.game.PlayerAnswerDTO;
import com.katiforis.top10.DTO.game.GameStateDTO;
import com.katiforis.top10.DTO.game.QuestionDTO;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
@Component
public class GameCache extends GenericCacheManager<String, GameStateDTO> {

    public boolean addAnswer(PlayerAnswerDTO playerAnswerDTO){

        synchronized (cache){
            GameStateDTO gameStateDTO =  getGameState(playerAnswerDTO.getGameId());

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
