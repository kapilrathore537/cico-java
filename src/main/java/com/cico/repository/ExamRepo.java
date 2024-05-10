package com.cico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cico.model.Exam;

public interface ExamRepo extends JpaRepository<Exam, Integer> {

	Optional<Exam> findByExamIdAndIsDeleted(Integer examId, boolean b);

	Exam findByExamNameAndIsDeleted(String examName, boolean b);

	List<Exam> findByIsDeleted(boolean b);

}
