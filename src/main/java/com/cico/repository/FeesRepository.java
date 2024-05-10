package com.cico.repository;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cico.model.Fees;
import com.cico.model.Student;

public interface FeesRepository extends JpaRepository<Fees, Integer> {

	Fees findByStudent(Student findByStudentId);

	Page<Fees> findAllByIsCompleted(boolean b, Pageable pageable);


	@Query("SELECT f FROM Fees f WHERE f.student.fullName LIKE %:fullName% AND f.isCompleted =:isCompleted")
	List<Fees> findByStudentFullNameContaining(@Param("fullName") String fullName,@Param("isCompleted") Boolean isCompleted);

	@Query("SELECT f FROM Fees f WHERE f.date BETWEEN :startDate AND :endDate AND f.isCompleted =:isCompleted ORDER BY f.date DESC")
	List<Fees> findFeesByGivenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,@Param("isCompleted") Boolean isCompleted);

	Fees findByFeesId(Integer feesId);

	@Transactional
	@Modifying
	@Query("UPDATE Fees f SET f.remainingFees =:newRemainingFees where f.feesId =:feesId  ")
	Fees updateRemainingFees(@Param("newRemainingFees") Double newRemainingFees, @Param("feesId") Integer feesId);

	@Query("SELECT f FROM Fees f WHERE f.remainingFees != 0")
	Page<Fees> findByRemainingFees(Pageable pageable);

	@Transactional
	@Modifying
	@Query("UPDATE Fees f SET f.isCompleted =1 where f.feesId =:feesId  ")
	public int updateIsCompleted(@Param("feesId") Integer feesId);
	
	@Transactional
	@Modifying
	@Query("UPDATE Fees f SET f.isCompleted =0 where f.feesId =:feesId  ")
	public int updateNotIsCompleted(@Param("feesId") Integer feesId);

	@Query("SELECT MONTH(f.payDate) AS month, SUM(f.feesPayAmount) AS totalPaid " +
		       "FROM FeesPay f " +
		       "WHERE YEAR(f.payDate) = :year " +
		       "GROUP BY MONTH(f.payDate) ")
		List<Object[]> getTotalFeesPaidByMonth(@Param("year") int year);

  
		@Query("SELECT SUM(f.finalFees) AS totalfees, SUM(f.remainingFees) AS pending, SUM(f.feesPaid) AS collected FROM Fees f")
		public List<Object[]> getTotalFeeCollection();

	@Query("SELECT f From Fees f where f.student.studentId =:studentId")
	Fees findFeesByStudentId(@Param("studentId") Integer studentId);

}
