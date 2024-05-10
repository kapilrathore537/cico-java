package com.cico.payload;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.Query;

import lombok.Data;

@Data
public class TodayLeavesRequestResponse {

	private Integer studentId;
	private String fullName;
	private String applyForCourse;
	private String profilePic;
	private String leaveReason;
	private LocalDate leaveDate;
	private LocalDate leaveEndDate;
	private Integer leaveDuration;
	private Integer leaveTypeId;
    private Integer leaveId;
    private String leaveTypeName;
    
    

	public TodayLeavesRequestResponse(  LocalDate leaveDate,  LocalDate leaveEndDate, Integer studentId, String fullName, String profilePic,String applyForCourse
			,Integer leaveTypeId, Integer leaveDuration, String leaveReason,Integer leaveId,  
			 String leaveTypeName) {
		super();
		this.studentId = studentId;
		this.fullName = fullName;
		this.applyForCourse = applyForCourse;
		this.profilePic = profilePic;
		this.leaveReason = leaveReason;
		this.leaveDate = leaveDate;
		this.leaveEndDate = leaveEndDate;
		this.leaveDuration = leaveDuration;
		this.leaveTypeId = leaveTypeId;
		this.leaveId = leaveId;
		this.leaveTypeName = leaveTypeName;
	}
    
    
}
