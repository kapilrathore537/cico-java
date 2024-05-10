package com.cico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.cico.model.TaskQuestion;

public interface TaskQuestionRepository extends JpaRepository<TaskQuestion, Long> {

	Optional<TaskQuestion> findByQuestionId(Long questionId);

	@Modifying
	@Transactional
	@Query("UPDATE TaskQuestion t set t.isDeleted =1  WHERE   t.questionId =:questionId ")
	void deleteTaskQuestion(@Param("questionId") Long questionId);
}
