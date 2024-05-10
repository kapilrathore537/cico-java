//package com.cico.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.cico.model.StudentTaskSubmittion;
//import com.cico.util.SubmissionStatus;
//
//@Repository
//public interface StudentTaskSubmittionRepository  extends JpaRepository<StudentTaskSubmittion,Integer>{
//
//	@Query("SELECT s FROM StudentTaskSubmittion s WHERE s.student.studentId=:studentId")
//	List<StudentTaskSubmittion> getSubmitedTaskForStudent(@Param("studentId") Integer studentId);
//
//	@Transactional
//	@Modifying
//	@Query("UPDATE StudentTaskSubmittion a SET a.status=:status , a.review=:review WHERE a.id=:id")
//	void updateSubmitTaskStatus(@Param("id") Integer submissionId,@Param("status") SubmissionStatus status,@Param("review") String review);
//
//	@Query("SELECT s FROM StudentTaskSubmittion s WHERE s.id=:id")
//	StudentTaskSubmittion findBySubmissionId(@Param("id") Integer submissionId);
//
//}
