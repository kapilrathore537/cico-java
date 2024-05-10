package com.cico.payload;

import com.cico.model.Student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentLoginResponse {

private	String token;
private	Boolean isDeviceIdDifferent;
private	Boolean isDeviceAlreadyInUse;
private	Boolean isFeesDue;
private Student studentData;
public StudentLoginResponse(String token, Boolean isDeviceIdDifferent, Boolean isDeviceAlreadyInUse,
		Boolean isFeesDue) {
	super();
	this.token = token;
	this.isDeviceIdDifferent = isDeviceIdDifferent;
	this.isDeviceAlreadyInUse = isDeviceAlreadyInUse;
	this.isFeesDue = isFeesDue;
}
	
}

