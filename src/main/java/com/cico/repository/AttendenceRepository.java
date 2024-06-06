package com.cico.repository;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cico.model.Attendance;

@Repository
public interface AttendenceRepository extends JpaRepository<Attendance, Integer> {

	public Attendance findByStudentIdAndCheckInDate(Integer studentId, LocalDate date);

	@Query("SELECT a FROM Attendance a WHERE a.studentId=:studentId AND a.checkInDate<:currentDate AND a.checkOutDate is null ORDER BY checkInDate DESC")
	Attendance findByStudentIdAndCheckInDateLessThanCurrentDate(Integer studentId, LocalDate currentDate);

	public Attendance findByStudentIdAndCheckInDateAndCheckOutDate(Integer studentId, LocalDate date,
			LocalDate checkOutDate);

	@Query(nativeQuery = true, value = "SELECT * FROM attendance WHERE student_id = :studentId AND check_in_date BETWEEN :startDate AND :endDate AND check_in_time IS NOT NULL AND check_out_time IS NOT NULL ORDER BY check_in_date DESC LIMIT :offset, :limit")
	public List<Attendance> findAttendanceHistory(@Param("studentId")Integer studentId,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate,@Param("offset") Integer offset,@Param("limit") Integer limit);
	
	@Query("SELECT a FROM Attendance a WHERE a.studentId=:studentId AND a.checkInDate between :startDate AND :endDate AND a.checkInTime IS NOT NULL AND a.checkOutTime IS NOT NULL")
	public Page<Attendance> findAttendanceHistory(Integer studentId, LocalDate startDate, LocalDate endDate,
			PageRequest of);

	@Query("SELECT a FROM Attendance a WHERE a.studentId=:studentId AND MONTH(a.checkInDate)=:monthNo AND a.checkOutTime IS NOT NULL")
	public List<Attendance> findByStudentIdAndMonthNo(@Param("studentId") Integer studentId,
			@Param("monthNo") Integer monthNo);

	@Query("SELECT a FROM Attendance a WHERE a.studentId=:id And a.checkInTime IS NOT NULL And a.checkOutTime IS NOT NULL")
	public Page<Attendance> findAllByStudentId(@Param("id") Integer id, PageRequest pageRequest);

	@Query("SELECT s.fullName, s.mobile, s.profilePic, s.applyForCourse, s.studentId, a.checkInTime "
			+ "FROM Student s " + "INNER JOIN Attendance a ON a.studentId = s.studentId " + "WHERE s.isCompleted = 0 "
			+ "AND a.checkInDate = :currentDate")
	public List<Object[]> getTodaysPresents(@Param("currentDate") LocalDate currentDate);

	@Query("SELECT s.fullName, s.mobile, s.profilePic, s.applyForCourse, s.studentId, a.checkOutTime "
			+ "FROM Student s " + "INNER JOIN Attendance a ON a.studentId = s.studentId " + "WHERE s.isCompleted = 0 "
			+ "AND a.checkInDate = :currentDate " + "AND a.workingHour < 32400")
	public List<Object[]> getTodaysEarlyCheckouts(@Param("currentDate") LocalDate currentDate);

	@Query("SELECT COUNT(a) FROM Attendance a WHERE MONTH(a.checkInDate) = :month")
	public Long countPresentStudentsByMonth(@Param("month") Integer month);

	@Query("SELECT MONTH(a.checkInDate) AS month, COUNT(a.attendanceId) AS count FROM Attendance a "
			+ "WHERE YEAR(a.checkInDate) = :year AND a.studentId=:studentId GROUP BY MONTH(a.checkInDate)")
	List<Object[]> getMonthWisePresentForYear(@Param("year") Integer year, @Param("studentId") Integer studentId);

	@Transactional
	@Modifying
	@Query("DELETE FROM Attendance a WHERE a.studentId=:id AND a.checkInDate=:now")
	public void deleteAttendanceToday(@Param("id") Integer id, @Param("now") LocalDate now);

	@Query("SELECT COUNT(a) FROM Attendance a WHERE  a.studentId=:studentId AND  MONTH(a.checkInDate) = MONTH(CURRENT_DATE) AND a.workingHour >= 32400 AND a.isMispunch = 0 AND YEAR(a.checkInDate) = YEAR(CURRENT_DATE) ")
	public Long countPresentStudentsForCurrentMonth( @Param("studentId") Integer studentId);

	@Query("SELECT COUNT(a) FROM Attendance a WHERE a.studentId = :studentId " +
		       "AND FUNCTION('MONTH', a.checkInDate) = FUNCTION('MONTH', CURRENT_DATE) " +
		       "AND FUNCTION('YEAR', a.checkInDate) = FUNCTION('YEAR', CURRENT_DATE) " +
		       "AND a.isMispunch = 1")
		public Long countTotalMishpunchForCurrentMonth(@Param("studentId") Integer studentId);


	@Query("SELECT COUNT(a) FROM Attendance a " +
		       "WHERE a.studentId = :studentId " +
		       "AND FUNCTION('MONTH', a.checkInDate) = FUNCTION('MONTH', CURRENT_DATE) " +
		       "AND FUNCTION('YEAR', a.checkInDate) = FUNCTION('YEAR', CURRENT_DATE) " +
		       "AND a.workingHour < 32400 " +
		       "AND a.isMispunch = 0 " +
		       "GROUP BY FUNCTION('MONTH', a.checkInDate)")
		public Long countTotalEarlyCheckOutForCurrentMonth(@Param("studentId") Integer studentId);

	
	
	@Query("SELECT COUNT(a) FROM Attendance a WHERE a.studentId = :studentId AND YEAR(a.checkInDate) >= YEAR(:joinDate) AND a.checkInDate <=CURRENT_DATE() AND a.workingHour >= 32400 AND a.isMispunch = 0")
	public Long countTotalPresentStudentsForCurrentYear(Integer studentId, LocalDate joinDate);

	@Query("SELECT COUNT(a) FROM Attendance a WHERE a.studentId = :studentId " +
		       "AND  YEAR(a.checkInDate)>= YEAR(:joinDate) AND a.checkInDate <= CURRENT_DATE() " +
		       "AND a.isMispunch = 1")
	public Long countTotalMishpunchForCurrentYear(Integer studentId, LocalDate joinDate);

	
	@Query("SELECT COUNT(a) FROM Attendance a " +
		       "WHERE a.studentId = :studentId " +
		       "AND  YEAR(a.checkInDate)>= YEAR(:joinDate) AND a.checkInDate <= CURRENT_DATE() " +
		       "AND a.workingHour < 32400 " +
		       "AND a.isMispunch = 0 ")
	public Long countTotalEarlyCheckOutForCurrentYear(Integer studentId, LocalDate joinDate);

	@Query("SELECT a FROM Attendance a WHERE a.studentId=:studentId AND MONTH(a.checkInDate)=:monthNo AND YEAR(a.checkInDate) =:year AND a.workingHour >=32400 AND a.isMispunch =0")
	public List<Attendance> findByStudentIdForCurrentMonth(@Param("studentId") Integer studentId,@Param("monthNo") Integer monthNo ,@Param("year") Integer year);

	@Query("SELECT a FROM Attendance a " +
		       "WHERE a.studentId = :studentId " +
		       "AND MONTH(a.checkInDate)=:monthNo " +
		       "AND YEAR(a.checkInDate)=:year "+
		       "AND a.workingHour < 32400 " +
		       "AND a.isMispunch = 0 ")
	public List<Attendance> countTotalEarlyCheckOutForCurrent1(Integer studentId, @Param("monthNo") Integer monthNo, @Param("year") Integer year);
	
	
	@Query("SELECT a FROM Attendance a WHERE a.studentId = :studentId " +
		       "AND MONTH(a.checkInDate)=:monthNo " +
		       "AND YEAR(a.checkInDate)=:year "+
		       "AND a.isMispunch = 1")
	public List<Attendance> countTotalMishpunchForCurrentYear1(Integer studentId,@Param("monthNo") Integer monthNo, @Param("year") Integer year);
	
	@Query("SELECT COUNT(a) FROM Attendance a WHERE a.checkInDate = CURRENT_DATE() AND a.workingHour < 32400 AND a.isMispunch = 0")
	public Long getTodayEarlyCheckOutsCount();
	
	@Query("SELECT COUNT(s) FROM Student s WHERE  s.isCompleted = 0 AND  s.studentId  NOT IN ("
			+ "SELECT a.studentId FROM Attendance a WHERE DATE(a.checkInDate) = CURRENT_DATE())  ")
	public Long getTodayAbsentCount();
	
	
}
