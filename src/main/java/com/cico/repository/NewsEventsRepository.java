package com.cico.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cico.model.NewsEvents;

@Repository
public interface NewsEventsRepository extends JpaRepository<NewsEvents, Integer> {
  
	@Query("SELECT u FROM NewsEvents u WHERE  u.id = :id AND u.isDeleted = false")
	Optional<NewsEvents>findById(@Param("id") Integer id);

	Page<NewsEvents> findAllByIsDeletedAndIsActive(boolean b,boolean a, PageRequest of);
	
	Page<NewsEvents> findAllByIsDeleted(boolean b, PageRequest of);
	
	@Transactional
	@Modifying
	@Query("UPDATE NewsEvents n SET n.isActive=:b WHERE id=:id")
	int updateActiveAndInActiveNewsAndEvent(@Param("b") boolean b,@Param("id") Integer id);


	Page<NewsEvents> findAllByTitleContainingOrShortDescriptionContaining(String search,
			String search2, PageRequest of);

	@Query("SELECT u FROM NewsEvents u WHERE (u.title LIKE CONCAT(:search, '%') OR u.shortDescription LIKE CONCAT(:search, '%')) AND u.isDeleted = false AND u.isActive = true")
	Page<NewsEvents> searchNewsAndEventForStudent(String search, PageRequest of);
	
	@Query("SELECT u FROM NewsEvents u WHERE (u.title LIKE CONCAT(:search, '%') OR u.shortDescription LIKE CONCAT(:search, '%')) AND u.isDeleted = false")
	Page<NewsEvents> searchNewsAndEventForAdmin(String search, PageRequest of);


}