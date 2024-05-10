package com.cico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cico.model.SubjectExam;
import com.cico.payload.ExamResultResponse;

@Repository
public interface SubjectExamRepo extends JpaRepository<SubjectExam, Integer> {

	@Query("SELECT  NEW com.cico.payload.ExamResultResponse( r.id, r.correcteQuestions , r.wrongQuestions ,  r.notSelectedQuestions , r.student.profilePic,r.student.studentId ,r.student.fullName,r.scoreGet , r.totalQuestion  )FROM  SubjectExam  se   JOIN se.results  r 	WHERE se.examId =:examId  GROUP BY se.examId ,r.id ")
	List<ExamResultResponse> findAllStudentResultWithExamId(Integer examId);

	Optional<SubjectExam> findByExamName(String examName);

	@Query("SELECT  s FROM SubjectExam s   RIGHT JOIN  s.results r WHERE  s.examId =:examId AND r.student.studentId =:studentId  ")
	Optional<SubjectExam> findByExamIdAndStudentId(Integer examId, Integer studentId);

}
