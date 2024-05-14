package com.cico.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cico.model.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {

	@Transactional
	@Modifying
	@Query("Update Course c Set isDeleted=1 Where c.courseId=:courseId")
	public int deleteCourse(@Param("courseId") Integer courseId);

	@Query("SELECT c.courseId, c.courseName ,t.imageName,COUNT(DISTINCT s), COUNT(DISTINCT b) ,t.id " + "FROM Course c "
			+ "LEFT JOIN c.subjects s ON s.isDeleted = :isDeleted "
			+ "LEFT JOIN c.batches b ON b.isDeleted = :isDeleted "
			+ "LEFT JOIN c.technologyStack t ON t.isDeleted = :isDeleted " + "WHERE c.isDeleted = :isDeleted "
			+ "GROUP BY c.courseId, c.courseName")
	public Page<Object[]> findAllByIsDeleted(@Param("isDeleted") Boolean isDeleted, PageRequest p);

	@Query("SELECT c From Course c  Where c.courseId =:courseId AND c.isDeleted = 0")
	public Optional<Course> findByCourseId(@Param("courseId") Integer courseId);

//ravisingh
	public List<Course> findAllByIsDeletedAndIsStarterCourse(boolean b, boolean c);

	@Query("SELECT c.courseId, c.courseName, COUNT(DISTINCT s), COUNT(DISTINCT b) " + "FROM Course c "
			+ "LEFT JOIN c.subjects s ON s.isDeleted = :isDeleted "
			+ "LEFT JOIN c.batches b ON b.isDeleted = :isDeleted " + "WHERE c.isDeleted = :isDeleted "
			+ "GROUP BY c.courseId, c.courseName")
	public List<Object[]> findByIsDeleted(@Param("isDeleted") boolean isDeleted);

	public List<Course> findBycourseIdInAndIsDeletedFalse(List<Integer> courseId);

	public Course findByCourseIdAndIsDeleted(Integer courseId, Boolean isDeleted);

	@Query("SELECT DISTINCT c.courseId, c.courseName, COUNT(DISTINCT s), COUNT(DISTINCT b), s.subjectName, s.technologyStack.imageName, b.batchName, b.subject.technologyStack.imageName, b.batchStartDate, b.batchTiming, b.batchDetails "
			+ "FROM Course c " + "LEFT JOIN c.subjects s ON s.isDeleted = 0 "
			+ "LEFT JOIN c.batches b ON b.isDeleted = 0 " + "WHERE c.isDeleted = 0 AND c.courseId = :courseId "
			+ "GROUP BY c.courseId, c.courseName, s.subjectName, s.technologyStack.imageName, b.batchName, b.subject.technologyStack.imageName, b.batchStartDate, b.batchTiming, b.batchDetails")
	public List<Object[]> getCoureWithBatchesAndSubjects(@Param("courseId") Integer courseId);

	@Query("SELECT c.courseId ,c.courseName , c.technologyStack.imageName ,c.technologyStack.id FROM Course c WHERE  c.isDeleted = 0 AND c.isStarterCourse = 0 ")
	public List<Object[]> getAllNonStarterCourses();

	@Query("SELECT c.courseId ,c.courseName , c.technologyStack.imageName ,c.technologyStack.id FROM Course c WHERE  c.isDeleted = 0 AND c.isStarterCourse = 1 ")
	public List<Object[]> getAllStarterCourses();
 
	public Course findByCourseNameAndIsDeletedFalse(String trim);

}