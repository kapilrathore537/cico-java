package com.cico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cico.model.StudentWorkReport;

@Repository
public interface StudentWorkReportRepository extends JpaRepository<StudentWorkReport, Integer>{

	StudentWorkReport findByAttendanceId(Integer attendanceId);

}