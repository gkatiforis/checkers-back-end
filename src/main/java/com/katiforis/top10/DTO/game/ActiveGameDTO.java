package com.katiforis.top10.DTO.game;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ActiveGameDTO implements Serializable {
    private long id;
    private Integer active_player;
    private Date date_started;
    List<QuestionDTO> questions;
    List<PlayerDTO> players;
    List<ActiveGameAnswerDTO> activeGameAnswers;
}
