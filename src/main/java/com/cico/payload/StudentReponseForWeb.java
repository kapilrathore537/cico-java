package com.cico.payload;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class StudentReponseForWeb {

	private Integer studentId;
	private String userId;
	private String fullName;
	private String mobile;
	private String email;
	private LocalDate dob;
	private String profilePic;
	private String currentCourse;
	private String applyForCourse;
	private LocalDate joinDate;
	private CourseResponse courseResponse;
	private Double finalFees;
	private Integer courseId;

	public StudentReponseForWeb(String fullName, Integer studentId) {
		this.studentId = studentId;
		this.fullName = fullName;
	}

	// s.fullName, s.mobile ,s.profilePic ,s.applyForCourse , s.studentId
	public StudentReponseForWeb(String fullName, String mobile, String profilePic, String applyForCourse,
			Integer studentId) {
		super();
		this.studentId = studentId;

		this.fullName = fullName;
		this.mobile = mobile;
		this.profilePic = profilePic;

		this.applyForCourse = applyForCourse;

	}
	// s.studentId ,s.userId ,s.fullName , s.mobile , s.email ,s.dob , profilePic
	// ,s.currentCourse ,s.currentCourse,c.courseId

	public StudentReponseForWeb(Integer studentId, String userId, String fullName, String mobile, String email,
			LocalDate dob, String profilePic, String currentCourse, String applyForCourse, Integer courseId,
			String courseName, LocalDate joinDate) {
		super();
		this.studentId = studentId;
		this.userId = userId;
		this.fullName = fullName;
		this.mobile = mobile;
		this.email = email;
		this.dob = dob;
		this.profilePic = profilePic;
		this.currentCourse = currentCourse;
		this.applyForCourse = applyForCourse;
		this.courseId = courseId;
		CourseResponse res = new CourseResponse();
		res.setCourseId(courseId);
		res.setCourseName(courseName);
		this.courseResponse = res;
		this.joinDate = joinDate;
	}

}
