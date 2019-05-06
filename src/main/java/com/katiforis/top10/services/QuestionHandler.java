package com.katiforis.top10.services;

import com.katiforis.top10.DTO.PlayerAnswer;
import com.katiforis.top10.model.Answer;
import com.katiforis.top10.model.Question;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuestionHandler {

    @Transactional
    List<Question> getQuestions();

    @Transactional
    Answer isAnswerValid(PlayerAnswer playerAnswerDTO);
}

