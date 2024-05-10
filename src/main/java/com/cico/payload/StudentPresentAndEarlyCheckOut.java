package com.cico.payload;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentPresentAndEarlyCheckOut {

	private String fullName;
	private String mobile;
	private String profilePic;
	private String applyForCourse;
	private Integer studentId;
	private LocalTime checkInTime;
	
}
