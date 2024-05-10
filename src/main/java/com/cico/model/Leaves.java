package com.cico.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "leaves")
@AllArgsConstructor
@NoArgsConstructor
public class Leaves {
	  
	@Id
	@GeneratedValue(strategy  = GenerationType.AUTO)
	private Integer leaveId;
	private Integer studentId;
	private Integer leaveTypeId;
	private LocalDate leaveDate;
	private LocalDate leaveEndDate;
	private String leaveReason;
	private String leaveDayType;
	private String halfDayType;
	private Integer leaveStatus=0;
	private Integer retractLeave=0;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime leaveStatusChangeDate;
	private String leaveDeclinedReason;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime createdDate;
	private Integer leaveDuration;
	
	
	
	
	public Leaves(Integer studentId, Integer leaveTypeId, String leaveReason, String leaveDayType) {
		super();
		this.studentId = studentId;
		this.leaveTypeId = leaveTypeId;
		this.leaveReason = leaveReason;
		this.leaveDayType = leaveDayType;
	}
	public Leaves(Integer studentId, Integer leaveTypeId, LocalDate leaveDate, String leaveReason,
			String leaveDayType) {
		super();
		this.studentId = studentId;
		this.leaveTypeId = leaveTypeId;
		this.leaveDate = leaveDate;
		this.leaveReason = leaveReason;
		this.leaveDayType = leaveDayType;
	}
	
}
	
	
