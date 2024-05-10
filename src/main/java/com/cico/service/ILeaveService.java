package com.cico.service;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface ILeaveService {

	public ResponseEntity<?>  getAllLeavesType();
	
	public ResponseEntity<?> addStudentLeave(HttpHeaders header, Integer leaveTypeId, String leaveStartDate, String leaveEndDate,
			String leaveReason, String leaveDayType, String halfDayType);

	public ResponseEntity<?> getStudentLeaves(Integer studentId,Integer page, Integer size);

	public Map<String, Object> deleteStudentLeave(HttpHeaders header, Integer leaveId);

	public Map<String, Object> retractStudentLeave(HttpHeaders header, Integer leaveId);

	public ResponseEntity<?> studentLeaveMonthFilterById(Integer studentId, Integer monthNo);

	public ResponseEntity<?> studentLeaveMonthFilterByToken(HttpHeaders header, Integer monthNo);
	
	public ResponseEntity<?> getStudentLeavesByToken(HttpHeaders header, Integer page, Integer size);

}
