package com.katiforis.top10.services.impl;//package me.xlui.im.services;

import com.katiforis.top10.DTO.game.PlayerAnswerDTO;
import com.katiforis.top10.model.Answer;
import com.katiforis.top10.model.Question;
import com.katiforis.top10.repository.QuestionRepository;
import com.katiforis.top10.services.QuestionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class QuestionHandlerImpl implements QuestionHandler {

	private static final Logger logger = LoggerFactory.getLogger(QuestionHandlerImpl.class);

	@Autowired
	QuestionRepository questionRepository;

	List<Question> questions;

	private static final int MIN = 3;

	/**
	 * Initialize the immutable question list
	 */
	@PostConstruct
	public void init(){
		questions = questionRepository.findAll();
	}

	public List<Question> getQuestions(){
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

		String[] answers = WordHandler.convert(playerAnswerDTO.getAnswer()).split("\\|");


		Question question = getQuestionById(playerAnswerDTO.getQuestionId());

		if (question == null) {
			return null;
		}

		List<Answer> correctAnswers = question.getAnswers();

		for (String answer : answers) {
			Answer correctAnswer = getCorrect(correctAnswers, answer);
			return correctAnswer;
		}

		return null;
	}

	private int getDiffChars(String correct, String answer){
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

		return correct.length() - match;
	}
	private boolean isItAMatch(String correct, String answer, List<Answer> correctAnswers) {

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

		return false;
	}


	private Answer getCorrect(List<Answer> correctAnswers, String answers) {

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
		return null;
	}


	private Question getQuestionById(Long id) {

		for (Question question : questions) {
			if (question.getId() == id) {
				return question;
			}
		}
		return null;
	}

}
