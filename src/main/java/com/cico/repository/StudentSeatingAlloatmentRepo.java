package com.cico.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cico.model.StudentSeatingAlloatment;

public interface StudentSeatingAlloatmentRepo extends JpaRepository<StudentSeatingAlloatment, Long> {

	@Query("SELECT a FROM StudentSeatingAlloatment a  WHERE a.seatAllocatedDate = :date")
	List<StudentSeatingAlloatment>findAll(@Param("date")LocalDate date);
	
	@Query("SELECT a FROM StudentSeatingAlloatment a WHERE a.student.studentId =:studentId")
	Optional< StudentSeatingAlloatment>  findByStudentId(@Param("studentId") Integer studentId);

	@Modifying
	@Transactional
	@Query("UPDATE   StudentSeatingAlloatment  a SET a.seatNumber =:seatNumber  , a.seatAllocatedDate = CURRENT_DATE WHERE a.student.studentId =:studentId  ")
	void updateSeatNumber(@Param("studentId") Integer studentId, @Param("seatNumber") int seatNumber);
 
	@Query("SELECT a FROM StudentSeatingAlloatment a WHERE a.student.studentId =:studentId AND a.seatAllocatedDate =:date")
	Optional< StudentSeatingAlloatment> findByStudentIdAndDate(@Param("studentId") Integer studentId,@Param("date") LocalDate date);
  
	@Modifying
	@Transactional
	@Query("UPDATE StudentSeatingAlloatment a SET a.seatNumber = 0 WHERE a.seatAllocatedDate != :date")
	void updateSeatNumber(@Param("date") LocalDate date);

}
