package com.cico.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student_work_report")
public class StudentWorkReport {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer workReportId;
	
	private Integer attendanceId;
	
	@Column(length = 10000)
	private String workReport;
	
	private String attachment;
	
	private LocalDateTime createdDate;

	public StudentWorkReport(Integer workReportId, Integer attendanceId, String workReport, LocalDateTime createdDate) {
		super();
		this.workReportId = workReportId;
		this.attendanceId = attendanceId;
		this.workReport = workReport;
		this.createdDate = createdDate;
	}
	
	
}

