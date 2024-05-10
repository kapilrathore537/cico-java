package com.cico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cico.model.MockInterview;

@Repository
public interface MockRepo extends JpaRepository<MockInterview, Integer>{
    
	@Query("SELECT m FROM MockInterview  m  WHERE m.mockDate = CURRENT_DATE() AND m.isCompleted =0")
	List<MockInterview> findbycurrentDay();
   
	@Query("SELECT m FROM MockInterview  m  WHERE m.mockDate = CURRENT_DATE() AND m.isCompleted = 0 AND m.student.studentId =:studentId")
	MockInterview findByStudentIdAndCurrentDate(Integer studentId);

}
