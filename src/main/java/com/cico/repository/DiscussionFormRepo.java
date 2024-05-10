package com.cico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cico.model.DiscusssionForm;


@Repository
public interface DiscussionFormRepo extends JpaRepository<DiscusssionForm, Integer> {
      
	@Query("SELECT d FROM DiscusssionForm d WHERE d.student.fullName LIKE %:name% ")
	List<DiscusssionForm> searching(@Param("name") String name);



}
