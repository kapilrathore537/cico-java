package com.cico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cico.model.OrganizationInfo;

@Repository
public interface OrganizationInfoRepository extends JpaRepository<OrganizationInfo, Integer> {

}
