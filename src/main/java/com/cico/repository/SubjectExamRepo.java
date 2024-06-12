package com.cico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cico.model.SubjectExam;
import com.cico.payload.ExamResultResponse;
import com.cico.util.ExamType;

@Repository
public interface SubjectExamRepo extends JpaRepository<SubjectExam, Integer> {

	@Query("SELECT  NEW com.cico.payload.ExamResultResponse( r.id, r.correcteQuestions , r.wrongQuestions ,  r.notSelectedQuestions , r.student.profilePic,r.student.studentId ,r.student.fullName,r.scoreGet , r.totalQuestion  )FROM  SubjectExam  se   JOIN se.results  r 	WHERE se.examId =:examId  GROUP BY se.examId ,r.id ")
	List<ExamResultResponse> findAllStudentResultWithExamId(Integer examId);

	Optional<SubjectExam> findByExamName(String examName);

	@Query("SELECT  s FROM SubjectExam s   RIGHT JOIN  s.results r WHERE  s.examId =:examId AND r.student.studentId =:studentId  ")
	Optional<SubjectExam> findByExamIdAndStudentId(Integer examId, Integer studentId);

	Optional<SubjectExam> findByExamNameAndIsDeletedFalseAndIsActiveTrue(String trim);

	Optional<SubjectExam> findByExamNameAndIsDeletedFalse(String trim);

	@Query(value = "SELECT * FROM subject_exam AS e WHERE e.exam_type ='SCHEDULEEXAM' AND e.is_deleted  = false AND e.is_start = false  ORDER BY e.created_date DESC LIMIT 1", nativeQuery = true)
	SubjectExam findLatestExam();

	@Query("SELECT COUNT(se) FROM SubjectExam se "
			+ "LEFT JOIN se.results as result "
			+ " WHERE result.student.studentId =:studentId AND  se.examType =:examType")
	Long fetchSubjectExamCount(@Param("examType") ExamType examType, @Param("studentId") Integer studentId);

	@Query("SELECT COUNT(se) FROM  Student student  "
			+ "JOIN student.course as c ON c.isDeleted=FALSE "
			+ "LEFT JOIN c.subjects as s ON s.isDeleted =FALSE "
			+ "LEFT JOIN s.exams as se "
			+ "WHERE se.examType =:examType AND se.isActive=TRUE AND se.isDeleted=FALSE AND student.studentId =:studentId ")
	Long fetchTotalExamCount(@Param("studentId") Integer studentId,@Param("examType") ExamType examType);
}
