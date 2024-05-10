package com.cico.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.cico.model.Student;
import com.cico.payload.ApiResponse;
import com.cico.payload.AttendenceOfMonth;
import com.cico.payload.CounsellingResponse;
import com.cico.payload.MockResponse;
import com.cico.payload.OnLeavesResponse;
import com.cico.payload.PageResponse;
import com.cico.payload.StudentReponseForWeb;
import com.cico.payload.StudentResponse;
import com.cico.payload.TodayLeavesRequestResponse;

public interface IStudentService {
	public Student getStudentByUserId(String userId);

	public Student getStudentByInUseDeviceId(String deviceId);

	public ResponseEntity<?> login(String userId, String password, String fcmId, String deviceId, String deviceType);

	public ResponseEntity<ApiResponse> approveDevice(String userId, String deviceId);

	public ResponseEntity<?> checkInCheckOut(String latitude, String longitude, String time, String type, String date,
			MultipartFile studentImage, MultipartFile attachment, String workReport, HttpHeaders header);

	public ResponseEntity<?> dashboard(HttpHeaders header);

	public ResponseEntity<?> studentMispunchRequest(HttpHeaders header, String time, String date, String workReport,
			MultipartFile attachment);

	public ResponseEntity<?> getStudentProfileApi(HttpHeaders header);

	public ResponseEntity<?> studentEarlyCheckoutRequest(HttpHeaders header, String latitude, String longitude,
			String time, String date, String type, String workReport, MultipartFile studentImage,
			MultipartFile attachment);

	public ResponseEntity<?> getStudentCheckInCheckOutHistory(HttpHeaders header, String startDate, String endDate,
			Integer offset, Integer limit);

	public ResponseEntity<?> studentChangePassword(HttpHeaders header, String oldPassword, String newPassword);

	public ResponseEntity<?> updateStudentProfile(HttpHeaders header, String fullName, String mobile, String dob,
			String email, MultipartFile profilePic);

	public Map<String, Object> getTodayAttendance(Integer studentId);

	public Map<String, Object> studentAttendanceMonthFilter(HttpHeaders header, Integer monthNo);

	public Map<String, Object> getCalenderData(Integer id, Integer month, Integer year);

	public Map<String, Object> getStudentData(Integer studentId);

	public ResponseEntity<?> getTotalTodayAbsentStudent(Integer pageSise, Integer pageNumber);

	public ResponseEntity<?> getTotalStudentInLeaves(Integer pageSise, Integer pageNumber);
	
	public  ResponseEntity<?>  getTotalTodaysLeavesRequest(Integer pageSise, Integer pageNumber);

	public Boolean approveStudentLeaveReqeust(Integer studentId,Integer leaveId, String status);

	public PageResponse<StudentReponseForWeb> getAllStudentData(Integer page, Integer size);

	public PageResponse<StudentReponseForWeb> searchStudentByName(String fullName, Integer pageNumber, Integer pageSize);

	public StudentResponse getStudentById(Integer studentId);

	public ResponseEntity<?> registerStudent(Student student);

	public ResponseEntity<?> getStudentProfileForWeb(Integer studentId);

	public ResponseEntity<?> updateStudent(Student student);

	public ResponseEntity<?> getStudentOverAllAttendanceData(Integer studentId, Integer pageSise, Integer pageNumber);


	public ResponseEntity<?> getTodaysPresentsAndEarlyCheckouts(String key);

	public ResponseEntity<?> getMonthwiseAttendence(Integer month);

	public ResponseEntity<?> getStudentsAttendanceDataForTv(String date);

	public ResponseEntity<?> getMonthwiseAdmissionCountForYear(Integer year);

	public ResponseEntity<?> getStudentPresentsAbsentsAndLeavesYearWise(Integer year, Integer studentId);

	public ResponseEntity<?> allStudent();

	public ResponseEntity<?> deleteTodayAttendance(Integer id);	
	
	public ResponseEntity<?> getTodaysPresentAbsentEarlyCheckOutsMispunchAndLeaves();

	public AttendenceOfMonth currentMonthAttendenceForDashBoard(Integer studentId, String status);
	
	public void fetchRandomStudentForMockInterview();
	
	public void fetchRandomStudentForCounselling();
	
	public void  checkMockIsCompleteOrNot();
	
	public void  checkCounsellingkIsCompleteOrNot();

	MockResponse checkMockForStudent(Integer studentId);

	CounsellingResponse checkCounsellingForStudent(Integer studentId);

	public ResponseEntity<?> approveStudentDevice(String userId, String deviceId);

	public ResponseEntity<?> studentDeviceChangeApi(String userId);

	public ResponseEntity<?> getStudentByIdForWeb(Integer studentId);

}
