package com.cico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cico.model.TechnologyStack;

@Repository
public interface TechnologyStackRepository extends JpaRepository<TechnologyStack, Integer> {

	TechnologyStack findByTechnologyName(String technologyName);

	@Query("SELECT ts FROM TechnologyStack ts WHERE ts.id = :TechnologyStackId AND ts.isDeleted = false")
	Optional<TechnologyStack> findById(@Param("TechnologyStackId") Integer technologyStackId);

	@Query("SELECT ts FROM TechnologyStack ts WHERE  ts.isDeleted = false")
	List<TechnologyStack> findAll();

}