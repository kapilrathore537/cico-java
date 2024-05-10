package com.cico.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students")
public class Student {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer studentId;
	private String userId;
	private String fullName;
	private String mobile;
	private LocalDate dob;
	private String email;
	@JsonIgnore
	private String password;
	private String college;
	private LocalDate joinDate;
	private String profilePic;
	private LocalDateTime createdDate;
	private String applyForCourse;
	private String currentSem;
	private String fathersName;
	private String mothersName;
	private String fathersOccupation;
	private String contactFather;
	private String contactMother;
	private String localAddress;
	private String parmanentAddress;
	private String languageKnown;
	private String currentCourse;
	private String fcmId;
	private String deviceId;
	private String inUseDeviceId;
	private String isDeviceApproved;
	private String deviceType;
	private Boolean isFromEnquiry=false;
	private Boolean isConverted=false;
	private Boolean isCompleted=false;
	private Boolean isActive =true;
	private String role;
	
	@OneToOne
	@JsonBackReference
	//@JoinTable(name = "student_course", joinColumns = @JoinColumn(name = "student_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
	private Course course;
	


}
