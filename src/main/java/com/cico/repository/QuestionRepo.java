package com.cico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cico.model.Question;

public interface QuestionRepo extends JpaRepository<Question, Integer> {

	Question findByQuestionContentAndIsDeleted(String question, Boolean b);

	Optional<Question> findByQuestionIdAndIsDeleted(Integer questionId, Boolean b);

	List<Question> findByIsDeleted(boolean b);
	
	List<Question> findByIsDeleted(Boolean b);

	
	@Query("UPDATE Question q set q.isSelected = true WHERE q IN :randomQuestionList ")
	void setQuestionIsSelectdTrue(@Param("randomQuestionList") List<Question> randomQuestionList);

}
