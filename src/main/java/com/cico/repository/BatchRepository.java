package com.cico.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cico.model.Batch;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Integer> {

	@Query("SELECT b FROM Batch b WHERE b.batchStartDate > :currentDate AND b.isDeleted=false")
	List<Batch> findAllByBatchStartDate(@Param("currentDate") LocalDate currentDate);

	List<Batch> findAllByIsDeleted(Boolean b);

	Batch findByBatchIdAndIsDeleted(Integer batchId, boolean b);

	Batch findByBatchNameAndIsDeletedFalse(String batchName);

	@Query(value = " SELECT * FROM Batch as b WHERE b.batches_course_id =:courseId ORDER BY b.batch_start_date ASC LIMIT 1 ", nativeQuery = true)
	Optional<Batch> findByCourseId(@Param("courseId") Integer courseId);

}
