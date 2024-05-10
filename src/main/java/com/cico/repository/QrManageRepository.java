package com.cico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.cico.model.QrManage;

public interface QrManageRepository extends JpaRepository<QrManage, Long>{

	QrManage findByUuid(String qrKey);

	QrManage findByUserId(String username);

	@Transactional
	@Modifying
	@Query("Delete from QrManage q where q.userId=:username")
	int deleteByUserId(@Param("username") String username);
	

}
