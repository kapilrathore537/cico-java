package com.cico.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cico.service.ILeaveService;
import com.cico.util.AppConstants;



@RestController
@RequestMapping("/leave")
@CrossOrigin("*")
public class LeaveController {

	@Autowired
	private ILeaveService leaveService;

	@GetMapping("/getLeavesType")
	public ResponseEntity<?> getAllLeavesType() {
		return leaveService.getAllLeavesType();
	}

	@PostMapping("/addStudentLeave")
	public ResponseEntity<?> addStudentLeave(@RequestHeader(name = AppConstants.AUTHORIZATION) HttpHeaders header,
			@RequestParam("leaveTypeId") Integer leaveTypeId, @RequestParam("leaveStartDate") String leaveStartDate,
			@RequestParam(value = "leaveEndDate") String leaveEndDate, @RequestParam("leaveReason") String leaveReason,
			@RequestParam("leaveDayType") String leaveDayType, @RequestParam("halfDayType") String halfDayType) {
		 
		return leaveService.addStudentLeave(header, leaveTypeId, leaveStartDate,
				leaveEndDate, leaveReason, leaveDayType, halfDayType);
	}
	
	@GetMapping("/getStudentLeaves")
	public ResponseEntity<?> getStudentLeavesByToken(@RequestHeader(name = AppConstants.AUTHORIZATION) HttpHeaders header,
			@RequestParam(name="page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page, @RequestParam(name="size",defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		return leaveService.getStudentLeavesByToken(header, page, size);
	}

	@GetMapping("/getStudentLeavesById")
	public ResponseEntity<?> getStudentLeavesById(@RequestParam("studentId") Integer studentId,
			@RequestParam(name="page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page, @RequestParam(name="size",defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		return leaveService.getStudentLeaves(studentId, page, size);
	}

	@PostMapping("/deleteStudentLeave")
	public ResponseEntity<Map<String, Object>> deleteStudentLeave(@RequestHeader(name = AppConstants.AUTHORIZATION) HttpHeaders header,
			@RequestParam("leaveId") Integer leaveId) {
		Map<String, Object> deleteStudentLeave = leaveService.deleteStudentLeave(header, leaveId);
		return ResponseEntity.ok(deleteStudentLeave);
	}

	@PostMapping("/retractStudentLeave")
	public ResponseEntity<Map<String, Object>> retractStudentLeave(@RequestHeader(name = AppConstants.AUTHORIZATION) HttpHeaders header,
			@RequestParam("leaveId") Integer leaveId) {
		Map<String, Object> retractStudentLeave = leaveService.retractStudentLeave(header, leaveId);
		return ResponseEntity.ok(retractStudentLeave);
	}

	@GetMapping("/studentLeaveMonthFilterById")
	public ResponseEntity<?> studentLeaveMonthFilterById(@RequestParam("studentId")Integer studentId,
			@RequestParam("monthNo") Integer monthNo) {
		 ResponseEntity<?> studentLeaveMonthFilter = leaveService.studentLeaveMonthFilterById(studentId, monthNo);
		return studentLeaveMonthFilter;
	}
	
	@GetMapping("/studentLeaveMonthFilter")
	public ResponseEntity<?> studentLeaveMonthFilterByToken(@RequestHeader(name = AppConstants.AUTHORIZATION) HttpHeaders header,
			@RequestParam("monthNo") Integer monthNo) {
		 ResponseEntity<?> studentLeaveMonthFilter = leaveService.studentLeaveMonthFilterByToken(header, monthNo);
		return studentLeaveMonthFilter;
	}
	
}

