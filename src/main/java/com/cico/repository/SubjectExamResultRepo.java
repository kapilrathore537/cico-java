package com.cico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cico.model.Student;
import com.cico.model.SubjectExamResult;

@Repository
public interface SubjectExamResultRepo extends JpaRepository<SubjectExamResult, Integer> {

	
	@Query("SELECT s FROM  SubjectExamResult s WHERE s.student =:student  AND  s.subjectExamId =:examId ")
	Optional<SubjectExamResult> findByExamIdAndStudentId(Integer examId, Student student);

//	@Query("SELECT se FROM SubjectExam  se   JOIN  SubjectExamResult sr   WHERE sr.student =:student  AND  se.examId =:examId ")
//	Optional<SubjectExamResult> findByExamIdAndStudentId(Integer examId, Student student);
}
