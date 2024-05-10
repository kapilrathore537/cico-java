package com.cico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cico.model.CounsellingInterview;

@Repository
public interface CounsellingRepo extends JpaRepository<CounsellingInterview, Integer>{
   
	@Query("SELECT c FROM CounsellingInterview  c  WHERE c.CounsellingDate = CURRENT_DATE() AND c.isCompleted =0")
	List<CounsellingInterview> findbycurrentDay();

	@Query("SELECT c FROM CounsellingInterview  c  WHERE c.CounsellingDate = CURRENT_DATE() AND c.isCompleted = 0 AND c.student.studentId =:studentId")
	CounsellingInterview findByStudentIdAndCurrentDate(Integer studentId);

}
