package com.cico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cico.model.Likes;
import com.cico.model.Student;

@Repository
public interface LikeRepo extends JpaRepository<Likes, Integer> {

	@Query("DELETE Likes  l   WHERE l.student=:student")
	int deleteLike(Student student);

}
