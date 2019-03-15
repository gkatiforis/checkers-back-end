package com.katiforis.top10.repository;

import com.katiforis.top10.model.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {
    Question findById(Long id);
    List<Question> findAll();
}