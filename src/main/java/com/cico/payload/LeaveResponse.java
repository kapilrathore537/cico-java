package com.cico.payload;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.cico.model.LeaveType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveResponse {

	private Integer leaveId;
	private Integer studentId;
	private LeaveType leaveType;
	private LocalDate leaveDate;
	private LocalDate leaveEndDate;
	private String leaveReason;
	private String leaveDayType;
	private String halfDayType;
	private Integer leaveStatus=0;
	private Integer retractLeave=0;
	private LocalDateTime leaveStatusChangeDate;
	private String leaveDeclinedReason;
	private LocalDateTime createdDate;
	private Integer leaveDuration;
	
	
}
