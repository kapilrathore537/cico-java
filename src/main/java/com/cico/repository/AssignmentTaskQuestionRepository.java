package com.cico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cico.model.AssignmentTaskQuestion;

@Repository
public interface AssignmentTaskQuestionRepository extends JpaRepository<AssignmentTaskQuestion, Integer> {

	Optional<AssignmentTaskQuestion> findByQuestionId(Long questionId);
	
	@Modifying
	@Transactional
	@Query("UPDATE AssignmentTaskQuestion a set a.isDeleted = 1 WHERE a.questionId =:questionId")
	void deleteQuestionByIdAndId(Long questionId);

	@Query("SELECT   a FROM  AssignmentTaskQuestion a   WHERE  a.isDeleted = 0 AND  a.questionId =:questionId")
	Optional<AssignmentTaskQuestion> findById(Long questionId);

	@Query("SELECT CASE WHEN COUNT(sb) > 0 THEN TRUE ELSE FALSE END " + "FROM AssignmentTaskQuestion a "
			+ "LEFT JOIN a.assignmentSubmissions sb "
			+ "WHERE a.questionId = :questionId AND sb.student.studentId = :studentId")
	boolean checkTaskSubmissionExistence(@Param("questionId") Long questionId, @Param("studentId") Integer studentId);

}
