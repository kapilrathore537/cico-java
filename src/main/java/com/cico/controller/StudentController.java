package com.cico.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cico.model.Student;
import com.cico.payload.OnLeavesResponse;
import com.cico.payload.PageResponse;
import com.cico.payload.StudentReponseForWeb;
import com.cico.payload.StudentReponseForWeb;
import com.cico.payload.StudentReponseForWeb;
import com.cico.payload.StudentReponseForWeb;
import com.cico.payload.StudentReponseForWeb;
import com.cico.payload.StudentResponse;
import com.cico.payload.TodayLeavesRequestResponse;
import com.cico.service.IStudentService;
import com.cico.util.AppConstants;

@RequestMapping("/student")
@RestController
@CrossOrigin("*")
public class StudentController {

//	@Autowired
//	AuthenticationManager manager;

	@Autowired
	private IStudentService studentService;

	@PostMapping("/studentLoginApi")
	public ResponseEntity<?> loginStudent(@RequestParam("userId") String userId,
			@RequestParam("password") String password, @RequestParam("fcmId") String fcmId,
			@RequestParam("deviceId") String deviceId, @RequestParam("deviceType") String deviceType) {

		return studentService.login(userId, password, fcmId, deviceId, deviceType);

	}

	@PostMapping("/registerStudent")
	public ResponseEntity<?> registerStudent(@RequestBody Student student) {
		return studentService.registerStudent(student);

	}

	// changes //06/05/24
	@PutMapping("/updateStudentApi")
	public ResponseEntity<?> updateStudent(@RequestBody Student student) {
		return studentService.updateStudent(student);

	}

	@PostMapping("/studentDeviceIdApprovalApi") // for request
	public ResponseEntity<?> approveDevice(@RequestParam("userId") String userId,
			@RequestParam("deviceId") String deviceId) {

		return studentService.approveDevice(userId, deviceId);

	}

	@PostMapping("/studentDeviceApprovalApi")
	public ResponseEntity<?> approveStudentDevice(@RequestParam("userId") String userId,
			@RequestParam("deviceId") String deviceId) {
		return studentService.approveStudentDevice(userId, deviceId);
	}

	@PostMapping("/studentDeviceChangeApi")
	public ResponseEntity<?> studentDeviceChangeApi(@RequestParam("userId") String userId) {
		return studentService.studentDeviceChangeApi(userId);
	}

	@PostMapping("/studentCheckInCheckOutApi")
	public ResponseEntity<?> checkInCheckOut(@RequestParam("lat") String latitude,
			@RequestParam("long") String longitude, @RequestParam("time") String time,
			@RequestParam("type") String type, @RequestParam("date") String date,
			@RequestPart("studentImage") MultipartFile studentImage,
			@RequestPart(name = "attachment", required = false) MultipartFile attachment,
			@RequestParam("workReport") String workReport, @RequestHeader HttpHeaders header) {

		return studentService.checkInCheckOut(latitude, longitude, time, type, date, studentImage, attachment,
				workReport, header);

	}

	@GetMapping("/studentDashboardApi")
	public ResponseEntity<?> studentDashboard(@RequestHeader HttpHeaders header) {
		return studentService.dashboard(header);
	}

	@PostMapping("/studentMispunchRequestApi")
	public ResponseEntity<?> studentMispunchRequest(
			@RequestHeader(name = AppConstants.AUTHORIZATION) HttpHeaders header, @RequestParam("time") String time,
			@RequestParam("date") String date, @RequestParam("workReport") String workReport,
			@RequestPart(name = "attachment", required = false) MultipartFile attechment) {

		return studentService.studentMispunchRequest(header, time, date, workReport, attechment);
	}

	@PostMapping("/studentEarlyCheckoutRequestApi")
	public ResponseEntity<?> studentEarlyCheckoutRequest(
			@RequestHeader(name = AppConstants.AUTHORIZATION) HttpHeaders header, @RequestParam("lat") String latitude,
			@RequestParam("long") String longitude, @RequestParam("time") String time,
			@RequestParam("date") String date, @RequestParam("type") String type,
			@RequestParam("workReport") String workReport, @RequestPart("studentImage") MultipartFile studentImage,
			@RequestPart(name = "attachment", required = false) MultipartFile attachment) {

		return studentService.studentEarlyCheckoutRequest(header, latitude, longitude, time, date, type, workReport,
				studentImage, attachment);
	}

	@GetMapping("/getStudentCheckInCheckOutHistory")
	public ResponseEntity<?> getStudentCheckInCheckOutHistory(
			@RequestHeader(name = AppConstants.AUTHORIZATION) HttpHeaders header,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
			@RequestParam(name = "offset") Integer offset, @RequestParam(name = "limit") Integer limit) {

		return studentService.getStudentCheckInCheckOutHistory(header, startDate, endDate, offset, limit);
	}

	@GetMapping("/getStudentProfileApi")
	public ResponseEntity<?> getStudentProfileApi(
			@RequestHeader(name = AppConstants.AUTHORIZATION) HttpHeaders header) {
		return studentService.getStudentProfileApi(header);
	}

	@PostMapping("/studentChangePasswordApi")
	public ResponseEntity<?> studentChangePassword(@RequestHeader HttpHeaders header,
			@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {

		return studentService.studentChangePassword(header, oldPassword, newPassword);

	}

	@PostMapping("/updateStudentProfileApi")
	public ResponseEntity<?> updateStudentProfileApi(@RequestHeader HttpHeaders header,
			@RequestParam("full_name") String fullName, @RequestParam("mobile") String mobile,
			@RequestParam("dob") String dob, @RequestParam("email") String email,
			@RequestPart("profile_pic") MultipartFile profilePic) {

		return studentService.updateStudentProfile(header, fullName, mobile, dob, email, profilePic);
	}

	@GetMapping("/getTodayAttendance/{studentId}")
	public ResponseEntity<Map<String, Object>> getTodayAttendance(@PathVariable Integer studentId) {
		Map<String, Object> todayAttendance = studentService.getTodayAttendance(studentId);
		return ResponseEntity.ok(todayAttendance);
	}

	@GetMapping("/studentAttendanceMonthFilter")
	public ResponseEntity<Map<String, Object>> studentAttendanceMonthFilter(
			@RequestHeader(name = AppConstants.AUTHORIZATION) HttpHeaders header,
			@RequestParam("monthNo") Integer monthNo) {
		Map<String, Object> studentAttendanceMonthFilter = studentService.studentAttendanceMonthFilter(header, monthNo);
		return ResponseEntity.ok(studentAttendanceMonthFilter);
	}

	@GetMapping("/getStudentCalenderData")
	public ResponseEntity<Map<String, Object>> getCalenderData(@Param("id") Integer id, @Param("month") Integer month,
			@Param("year") Integer year) {
		Map<String, Object> response = studentService.getCalenderData(id, month, year);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@GetMapping("/getStudentData/{studentId}")
	public ResponseEntity<Map<String, Object>> getStudentData(@PathVariable("studentId") Integer studentId) {
		Map<String, Object> studentData = studentService.getStudentData(studentId);
		return new ResponseEntity<Map<String, Object>>(studentData, HttpStatus.OK);
	}

	@GetMapping("/getAllStudentData")
	public PageResponse<StudentReponseForWeb> getAllStudentData(
			@RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

		return studentService.getAllStudentData(page, size);
	}

	@GetMapping("/getStudentById")
	public ResponseEntity<StudentResponse> getStudentById(@RequestParam(name = "studentId") Integer studentId) {
		StudentResponse studentById = studentService.getStudentById(studentId);
		return new ResponseEntity<StudentResponse>(studentById, HttpStatus.OK);
	}

	@GetMapping("/getStudentByIdForWeb")
	public ResponseEntity<?> getStudentByIdForWeb(@RequestParam(name = "studentId") Integer studentId) {
		return studentService.getStudentByIdForWeb(studentId);

	}

	@GetMapping("/searchStudentByName")
	public ResponseEntity<PageResponse<StudentReponseForWeb>> searchStudentByName(
			@RequestParam(name = "fullName") String fullName, @RequestParam("pageSize") Integer pageSize,
			@RequestParam("pageNumber") Integer pageNumber) {
		PageResponse<StudentReponseForWeb> res = studentService.searchStudentByName(fullName, pageNumber, pageSize);
		return new ResponseEntity<PageResponse<StudentReponseForWeb>>(res, HttpStatus.OK);

	}

	// getting total absent student today
	@GetMapping("/getTotalTodayAbsentStudentAndPresent")
	public ResponseEntity<?> getTotalTodayAbsentStudent(@RequestParam("pageSize") Integer pageSise,
			@RequestParam("pageNumber") Integer pageNumber) {
		return studentService.getTotalTodayAbsentStudent(pageSise, pageNumber);

	}

	// getting total student who are in currently leave
	@GetMapping("/getTotalStudentInLeaves")
	public ResponseEntity<?> getTotalStudentInLeaves(@RequestParam("pageSize") Integer pageSise,
			@RequestParam("pageNumber") Integer pageNumber) {
		return studentService.getTotalStudentInLeaves(pageSise, pageNumber);

	}

	// getting all leaves request
	@GetMapping("/getTotalStudentTodaysInLeaves")
	public ResponseEntity<?> getTotalStudentTodaysInLeaves(@RequestParam("pageSize") Integer pageSise,
			@RequestParam("pageNumber") Integer pageNumber) {
		return studentService.getTotalTodaysLeavesRequest(pageSise, pageNumber);

	}

	// approve leave Request
	@PutMapping("/approveStudentLeaveReqeust/{studentId}/{leaveId}/{status}")
	public ResponseEntity<Boolean> approveStudentLeaveReqeust(@PathVariable("studentId") Integer studentId,
			@PathVariable("leaveId") Integer leaveId, @PathVariable("status") String Leavestatus) {
		Boolean status = studentService.approveStudentLeaveReqeust(studentId, leaveId, Leavestatus);
		return new ResponseEntity<Boolean>(status, HttpStatus.OK);
	}

	// get student data for web profile
	@GetMapping("/getStudentForWebStudentProfile")
	public ResponseEntity<?> getStudentProfileForWeb(@RequestParam("studentId") Integer studentId) {
		return studentService.getStudentProfileForWeb(studentId);
	}

	//
	@GetMapping("/getStudentOverAllAttendanceAndLeavesAndAbsents")
	public ResponseEntity<?> getStudentOverAllAttendanceData(@RequestParam("studentId") Integer studentId,
			@RequestParam("pageSize") Integer pageSise, @RequestParam("pageNumber") Integer pageNumber) {
		return studentService.getStudentOverAllAttendanceData(studentId, pageSise, pageNumber);
	}

	@GetMapping("/getTodaysPresentsAndEarlyCheckouts")
	public ResponseEntity<?> getTodaysPresentsAndEarlyCheckouts(@RequestParam("key") String key) {
		return studentService.getTodaysPresentsAndEarlyCheckouts(key);
	}

	@GetMapping("/getMonthwiseAttendence")
	public ResponseEntity<?> getMonthwiseAttendence(@RequestParam("month") Integer month) {
		return studentService.getMonthwiseAttendence(month);
	}

	@GetMapping("/getStudentsAttendanceDataForTv")
	public ResponseEntity<?> getStudentsAttendanceDataForTv(
			@RequestParam(name = "date", required = false) String date) {
		return studentService.getStudentsAttendanceDataForTv(date);
	}

	@GetMapping("/getMonthwiseAdmissionCountForYear")
	public ResponseEntity<?> getMonthwiseAdmissionCountForYear(@RequestParam("year") Integer year) {
		return studentService.getMonthwiseAdmissionCountForYear(year);
	}

	@GetMapping("/getStudentPresentsAbsentsAndLeavesYearWise")
	public ResponseEntity<?> getStudentPresentsAbsentsAndLeavesYearWise(@RequestParam("year") Integer year,
			@RequestParam("studentId") Integer studentId) {
		return studentService.getStudentPresentsAbsentsAndLeavesYearWise(year, studentId);
	}

	@GetMapping("/allStudent")
	public ResponseEntity<?> allStudent() {
		return studentService.allStudent();
	}

	@GetMapping("/deleteTodayAttendance/{id}")
	public ResponseEntity<?> deleteTodayAttendance(@PathVariable("id") Integer id) {
		return studentService.deleteTodayAttendance(id);
	}

	@GetMapping("/todayAttendanceCountsForAdmin")
	public ResponseEntity<?> todayAttendanceCountsForAdminDash() {
		return studentService.getTodaysPresentAbsentEarlyCheckOutsMispunchAndLeaves();
	}

	@GetMapping("/totalAttendaceAndLeaveDataOfStudentAfterJoining")
	public ResponseEntity<?> totalAttendaceAndLeaveDataOfStudentAfterJoining(@RequestParam("id") Integer studentId) {
		return new ResponseEntity<>(studentService.currentMonthAttendenceForDashBoard(studentId, "CURRENT_YEAR"),
				HttpStatus.OK);
	}

}
