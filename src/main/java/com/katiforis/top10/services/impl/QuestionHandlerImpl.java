package com.katiforis.top10.services.impl;

import com.katiforis.top10.DTO.game.PlayerAnswerDTO;
import com.katiforis.top10.model.Answer;
import com.katiforis.top10.model.Question;
import com.katiforis.top10.repository.QuestionRepository;
import com.katiforis.top10.services.QuestionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuestionHandlerImpl implements QuestionHandler {

	@Autowired
	QuestionRepository questionRepository;

	List<Question> questions;

	private static final int MIN = 3;

	/**
	 * Initialize the immutable question list
	 */
	@PostConstruct
	public void init(){
		log.debug("Start QuestionHandlerImpl.addAnswer");
		questions = questionRepository.findAll();
		log.debug("Start QuestionHandlerImpl.addAnswer");
	}

	public List<Question> getQuestions(){
		log.debug("Start QuestionHandlerImpl.addAnswer");
		return questions.stream()
				.collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
					Collections.shuffle(collected);
					return collected.stream();
				}))
				.limit(5)
				.collect(Collectors.toList());
	}



	@Transactional
	@Override
	public Answer isAnswerValid(PlayerAnswerDTO playerAnswerDTO) {
		log.debug("Start QuestionHandlerImpl.isAnswerValid");
		String[] answers = WordHandler.convert(playerAnswerDTO.getDescription()).split("\\|");


		Question question = getQuestionById(playerAnswerDTO.getQuestionId());

		if (question == null) {
			return null;
		}

		List<Answer> correctAnswers = question.getAnswers();

		for (String answer : answers) {
			Answer correctAnswer = getCorrect(correctAnswers, answer);
			return correctAnswer;
		}
		log.debug("End QuestionHandlerImpl.isAnswerValid");
		return null;
	}

	private int getDiffChars(String correct, String answer){
		log.debug("Start QuestionHandlerImpl.getDiffChars");
		if(correct.length() != answer.length()){
			return -1;
		}
		char [] correctCh =correct.toCharArray();
		char [] answerCh =answer.toCharArray();

		int match = 0;
		for(int i = 0; i<correct.length(); i++){
			if(correctCh[i] == answerCh[i]){
				match++;
			}
		}
		log.debug("End QuestionHandlerImpl.getDiffChars");
		return correct.length() - match;
	}
	private boolean isItAMatch(String correct, String answer, List<Answer> correctAnswers) {
		log.debug("Start QuestionHandlerImpl.isItAMatch");
		if (correct.equalsIgnoreCase(answer)) {
			return true;
		}


		float len = correct.length();
		if (len <= MIN - 1) {
			return false;
		}

		if (answer.length() < MIN) {
			return false;
		}

		int mustFindFirst = (int) Math.floor(len / 2);

		if (mustFindFirst < MIN) {
			mustFindFirst = MIN;
		}

		String mustBeginWith = correct.substring(0, mustFindFirst);

		if (answer.startsWith(mustBeginWith)) {
			List<String> answers = new ArrayList<>();

			for(Answer correctAnswer :correctAnswers){

				String[] descriptionArray = WordHandler.convert(correctAnswer.getDescription().trim()).split("\\|");
				for (String description : descriptionArray) {
					description = description.replaceAll("\u200E", "").trim();
					if(description.startsWith(mustBeginWith)){
						answers.add(description);
					}
				}
			}

			if (answers.size() > 1) {

				int offset = 0;
				do {

					int matches = 0;


					for (String aa : answers) {
						String des = aa.toUpperCase();
						int index = mustFindFirst + offset;
						if (!(index >= des.length() - 1)) {
							char ch = des.substring(index, index + 1).charAt(0);

							if (answer.length() > index && ch == answer.charAt(index)) {
								matches++;
							}
						}
					}

					if (matches == 1 && correct.startsWith(answer)) {
						return true;
					} else if (matches == 0) {
						return false;
					}
					offset++;
				} while (true);


			} else {
				return true;
			}
		}

		if( getDiffChars(correct, answer) == 1) {
			return true;
		}
		log.debug("End QuestionHandlerImpl.isItAMatch");
		return false;
	}


	private Answer getCorrect(List<Answer> correctAnswers, String answers) {
		log.debug("Start QuestionHandlerImpl.getCorrect");
		for (Answer correctAnswer : correctAnswers) {
			String[] descriptionArray = WordHandler.convert(correctAnswer.getDescription().trim()).split("\\|");
			for (String description : descriptionArray) {
				description = description.replaceAll("\u200E", "").trim();

				List<String> splittedAnswer = Arrays.asList(answers.split("\\s+"));

				for (String answer : splittedAnswer) {
					answer = answer.trim();
					if (!answer.isEmpty()) {
						if(isItAMatch(description, answer, correctAnswers)){
							return correctAnswer;
						}

					}
				}
			}
		}
		log.debug("End QuestionHandlerImpl.getCorrect");
		return null;
	}


	private Question getQuestionById(Long id) {
		log.debug("Start QuestionHandlerImpl.getQuestionById");
		for (Question question : questions) {
			if (question.getId() == id) {
				return question;
			}
		}
		log.debug("End QuestionHandlerImpl.getQuestionById");
		return null;
	}

}
