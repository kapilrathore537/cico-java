package com.cico.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cico.model.Leaves;

@Repository
public interface LeaveRepository extends JpaRepository<Leaves, Integer> {
	@Query("SELECT l FROM Leaves l WHERE l.studentId=:studentId")
	public Page<Leaves> findStudentLeaves(Integer studentId, PageRequest of);

	@Query("SELECT COUNT(l) FROM Leaves l WHERE l.studentId=:studentId")
	public int countByStudentId(Integer studentId);

	public Leaves findByStudentIdAndLeaveId(Integer studentId, Integer leaveId);

	@Transactional
	@Modifying
	@Query("delete from Leaves l  where l.studentId=:studentId And l.leaveId=:leaveId")
	public int deleteByStudnetIdLeaveId(@Param("studentId") Integer studentId, @Param("leaveId") Integer leaveId);

	@Transactional
	@Modifying
	@Query("UPDATE  Leaves l SET l.retractLeave=1 where l.studentId=:studentId And l.leaveId=:leaveId")
	public int deleteByStudnetIdLeaveIdStudentId(@Param("studentId") Integer studentId,
			@Param("leaveId") Integer leaveId);

	@Query("SELECT l FROM Leaves l WHERE l.studentId=:studentId AND MONTH(l.leaveDate)=:monthNo")
	public Page<Leaves> findByStudentIdAndMonthNo(@Param("studentId") Integer studentId,
			@Param("monthNo") Integer monthNo, PageRequest of);

	@Query("SELECT u FROM Leaves u where u.studentId =:id And u.leaveStatus = 1 ")
	public List<Leaves> findAllByStudentId(@Param("id") Integer id);

	@Transactional
	@Modifying
	@Query("UPDATE Leaves  l SET l.leaveStatus=:status WHERE l.studentId =:studentId AND l.leaveId =:leaveId ")
	public int updateStudentLeaves(@Param("studentId") Integer studentId,@Param("status") Integer status, @Param("leaveId") Integer leaveId );
    
	public Leaves findByStudentId(Integer StudentId);
	
	@Query("SELECT l from Leaves l WHERE l.studentId=:studentId AND l.leaveStatus=:active")
	public Page<Leaves> getStudentAllLeavesAndApproved(@Param("studentId") Integer studentId,@Param("active") Integer active, PageRequest pageRequest);
	
	@Query("SELECT COUNT(l) FROM Leaves l WHERE MONTH(l.leaveDate) = :month and DAY(l.leaveDate)!=7 and l.leaveDayType='Full Day'")
	public Long countLeaveStudentsByMonth(@Param("month") Integer month);

	@Query("SELECT MONTH(l.leaveDate) AS month, SUM(l.leaveDuration) AS total_leave_days  FROM  Leaves l WHERE YEAR(l.leaveDate) =:year AND l.studentId=:studentId AND l.leaveDayType='Full Day' AND l.leaveStatus=1 GROUP BY MONTH(l.leaveDate)")
	public List<Object[]> getMonthWiseLeavesForYear(@Param("year") Integer year,@Param("studentId") Integer studentId);

	@Query("SELECT l FROM Leaves l WHERE l.studentId =:studentId AND :startDate BETWEEN l.leaveDate AND l.leaveEndDate")
	public Optional<Leaves> findByStudentIdAndLeaveStartDateAndEndDate(@Param("studentId") Integer studentId,@Param("startDate") LocalDate startDate);
  
	@Query("SELECT COUNT(l) FROM Leaves l " +
		       "WHERE FUNCTION('MONTH', l.leaveDate) = FUNCTION('MONTH', CURRENT_DATE) " +
		       "AND FUNCTION('YEAR', l.leaveDate) = FUNCTION('YEAR', CURRENT_DATE) " +
		       "AND l.studentId = :studentId " +
		       "AND l.leaveDayType = 'Full Day' " +
		       "AND l.leaveStatus = 1 " +
		       "GROUP BY FUNCTION('MONTH', l.leaveDate)")
		public Long countTotalLeavesForCurrentMonth(@Param("studentId") Integer studentId);

	
	@Query("SELECT COUNT(l) FROM Leaves l " +
		       "WHERE  YEAR(l.leaveDate)>= YEAR(:joinDate) AND l.leaveDate <= CURRENT_DATE()" +
		       "AND l.studentId = :studentId " +
		       "AND l.leaveDayType = 'Full Day' " +
		       "AND l.leaveStatus = 1 ")
	public Long countTotalLeavesForCurrentYear(Integer studentId, LocalDate joinDate);

	
	
	@Query("SELECT u FROM Leaves u where u.studentId =:id And u.leaveStatus = 1 AND MONTH(u.leaveDate)= :month  AND YEAR(u.leaveDate) =:year")
	public List<Leaves> findAllByStudentIdForCurrentMonth(@Param("id") Integer id,@Param("month")Integer month,@Param("year") Integer year);
	
}
