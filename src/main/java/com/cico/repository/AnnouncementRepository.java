package com.cico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cico.model.Announcement;
import com.cico.model.Course;
import com.cico.model.Student;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long>{

	@Query("SELECT a FROM Announcement a WHERE :course MEMBER OF a.course AND :student NOT MEMBER OF a.students ORDER  BY a.date DESC ")
	public List<Announcement> getAnnouncementForStudentByCourse(@Param("course") Course course,@Param("student") Student student);

	@Query("SELECT COUNT(a) FROM Announcement a WHERE :course MEMBER OF a.course AND :student NOT MEMBER OF a.students")
	public Long countUnseenNotificationForStudent(Course course, Student student);

}