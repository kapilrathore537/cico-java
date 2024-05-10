package com.cico.payload;

import java.time.LocalDate;

import lombok.Data;

@Data
public class OnLeavesResponse {
 
	private String profilePic;
	private LocalDate leaveDate;
	private LocalDate leaveEndDate;
	private String applyForCourse;
	private String name;
	private Integer studentId;
	
	//l.leaveDate,l.leaveEndDate,s.profilePic ,s.applyForCourse ,s.setStudentId,s.name
	
	public OnLeavesResponse(   LocalDate leaveDate, LocalDate leaveEndDate,String profilePic, String applyForCourse,Integer studentId,String name) {
		super();
		this.profilePic = profilePic;
		this.leaveDate = leaveDate;
		this.leaveEndDate = leaveEndDate;
		this.applyForCourse = applyForCourse;
		this.name = name;
		this.studentId = studentId;
	}
	
	
}
