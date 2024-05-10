package com.cico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cico.model.Sessions;

@Repository
public interface SessionRepo extends JpaRepository<Sessions, Integer> {

}
