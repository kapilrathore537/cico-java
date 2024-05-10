package com.cico.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cico.model.Fees;
import com.cico.model.FeesPay;

public interface FeesPayRepository extends JpaRepository<FeesPay, Integer> {

	List<FeesPay> findByFees(Fees fees);
	
	@Query("SELECT f FROM FeesPay f WHERE f.feesPayAmount != 0")
	Page<FeesPay> findByFeesPayAmount(Pageable pageable);

	FeesPay findByPayId(Integer payId);

	@Query("SELECT f FROM FeesPay f WHERE f.fees.student.fullName LIKE %:fullName%")
	List<FeesPay> findByFullName(String fullName);
	
    @Query("SELECT f FROM FeesPay f WHERE f.payDate BETWEEN :startDate AND :endDate ORDER BY f.payDate DESC")
	List<FeesPay> findByMonth(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
	

}
