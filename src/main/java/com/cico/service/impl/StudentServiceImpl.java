
package com.cico.service.impl;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cico.exception.ResourceAlreadyExistException;
import com.cico.exception.ResourceNotFoundException;
import com.cico.kafkaServices.KafkaProducerService;
import com.cico.model.Attendance;
import com.cico.model.CounsellingInterview;
import com.cico.model.Course;
import com.cico.model.Leaves;
import com.cico.model.MockInterview;
import com.cico.model.OrganizationInfo;
import com.cico.model.QrManage;
import com.cico.model.Student;
import com.cico.model.StudentSeatingAlloatment;
import com.cico.model.StudentWorkReport;
import com.cico.payload.ApiResponse;
import com.cico.payload.AttendanceLogResponse;
import com.cico.payload.AttendenceOfMonth;
import com.cico.payload.CheckinCheckoutHistoryResponse;
import com.cico.payload.CheckoutResponse;
import com.cico.payload.CounsellingResponse;
import com.cico.payload.CourseResponse;
import com.cico.payload.DashboardResponse;
import com.cico.payload.MispunchResponse;
import com.cico.payload.MockResponse;
import com.cico.payload.NotificationInfo;
import com.cico.payload.OnLeavesResponse;
import com.cico.payload.PageResponse;
import com.cico.payload.StudentCalenderResponse;
import com.cico.payload.StudentLoginResponse;
import com.cico.payload.StudentPresentAndEarlyCheckOut;
import com.cico.payload.StudentReponseForWeb;
import com.cico.payload.StudentResponse;
import com.cico.payload.StudentTvResponse;
import com.cico.payload.TodayLeavesRequestResponse;
import com.cico.repository.AttendenceRepository;
import com.cico.repository.CounsellingRepo;
import com.cico.repository.CourseRepository;
import com.cico.repository.LeaveRepository;
import com.cico.repository.MockRepo;
import com.cico.repository.OrganizationInfoRepository;
import com.cico.repository.QrManageRepository;
import com.cico.repository.StudentRepository;
import com.cico.repository.StudentSeatingAlloatmentRepo;
import com.cico.repository.StudentWorkReportRepository;
import com.cico.security.JwtUtil;
import com.cico.service.IFileService;
import com.cico.service.IStudentService;
import com.cico.util.AppConstants;
import com.cico.util.HelperService;
import com.cico.util.NotificationConstant;
import com.cico.util.Roles;

@Service
public class StudentServiceImpl implements IStudentService {

	public static final String ANDROID = "Android";
	public static final String IOS = "iOS";
	public static final Integer TIME_PERIOD_NINE_HOURS = 9 * 60 * 60; // working hours

	@Autowired
	private StudentRepository studRepo;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private QrManageRepository qrManageRepository;

	@Autowired
	private LeaveRepository leaveRepository;

	@Autowired
	private AttendenceRepository attendenceRepository;

	@Autowired
	private OrganizationInfoRepository organizationInfoRepository;

	@Autowired
	private StudentWorkReportRepository workReportRepository;

	@Autowired
	private JwtUtil util;

	@Autowired
	private IFileService fileService;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private StudentSeatingAlloatmentRepo studentSeatingAlloatmentRepo;

	@Autowired
	private MockRepo mockRepo;

	@Autowired
	private CounsellingRepo counsellingRepo;

	@Autowired
	private KafkaProducerService kafkaProducerService;

	public Student getStudentByUserId(String userId) {
		return studRepo.findByUserId(userId);
	}

	public Student getStudentByInUseDeviceId(String deviceId) {
		return studRepo.findByInUseDeviceId(deviceId);
	}

//	@Override
//	public ResponseEntity<?> login1(String userId, String password, String fcmId, String deviceId,
//			String deviceType) {
//		Map<String, Object> response = new HashMap<>();
//		if (deviceType.equals(ANDROID) || deviceType.equals(IOS)) {
//			Student studentByUserId = getStudentByUserId(userId);
//			Student studentByInUseDeviceId = getStudentByInUseDeviceId(deviceId);
//			StudentLoginResponse studentResponse = new StudentLoginResponse();
//			
//			if (studentByUserId != null && studentByUserId.getPassword().equals(password)) {
//				
//				if(!studentByUserId.getInUseDeviceId().equals(deviceId)&&!studentByUserId.getInUseDeviceId().equals("")) {
//					if( studentByInUseDeviceId != null) {
//						studentResponse.setIsDeviceAlreadyInUse(true);
//
//					}else {
//						studentResponse.setIsDeviceAlreadyInUse(false);	
//					}
//					studentResponse.setToken(null);
//					studentResponse.setIsDeviceIdDifferent(true);
//					studentResponse.setIsFeesDue(false);
//					response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
//					response.put("student", studentResponse);
//					return new ResponseEntity<>(response,HttpStatus.OK);
//				}
//				if (studentByUserId.getIsActive()) {
//					if (studentByInUseDeviceId != null) {
//
//						if (userId.equals(studentByInUseDeviceId.getUserId())) {
//							System.out.println("1 case");
//							String token = util.generateTokenForStudent(studentByUserId.getStudentId().toString(),
//									studentByUserId.getUserId(), deviceId, Roles.STUDENT.toString());
//
//							StudentLoginResponse studentResponse2 = new StudentLoginResponse(token, false, false, false);
//							studentResponse2.setStudentData(studentByUserId);
//							response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
//							response.put("student", studentResponse2);
//							return new ResponseEntity<>(response,HttpStatus.OK);
//						}
//
//						else {
//							System.out.println("2 case");
//							studentResponse.setToken(null);
//							studentResponse.setIsDeviceIdDifferent(true);
//							studentResponse.setIsFeesDue(false);
//							studentResponse.setIsDeviceAlreadyInUse(true);
//
//							response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
//							response.put("student", studentResponse);
//							return new ResponseEntity<>(response,HttpStatus.OK);
//						}
//
//					}
//
//					else {
//						if ((studentByUserId.getInUseDeviceId() == null)
//								|| (studentByUserId.getDeviceId().trim().equals(""))) {
////							System.out.println("3 case");
////							studentByUserId.setInUseDeviceId(deviceId);
////							studentByUserId.setFcmId(fcmId);
////							studentByUserId.setDeviceType(deviceType);
////							studRepo.save(studentByUserId);
//
//							String token = util.generateTokenForStudent(studentByUserId.getStudentId().toString(),
//									studentByUserId.getUserId(), deviceId, Roles.STUDENT.toString());
//
//							studentResponse.setToken(token);
//							studentResponse.setIsDeviceIdDifferent(false);
//							studentResponse.setIsDeviceAlreadyInUse(false);
//							studentResponse.setIsFeesDue(false);
//							studentResponse.setStudentData(studentByUserId);
//							response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
//							response.put("student", studentResponse);
//							return new ResponseEntity<>(response,HttpStatus.OK);
//						}
//
//						else {
//							System.out.println("4 case");
//							studentResponse.setToken("");
//							studentResponse.setIsDeviceIdDifferent(true);
//							studentResponse.setIsDeviceAlreadyInUse(false);
//							studentResponse.setIsFeesDue(false);
//
//							response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
//							response.put("student", studentResponse);
//							return new ResponseEntity<>(response,HttpStatus.OK);
//
//						}
//					}
//				} else {
//					response.put(AppConstants.MESSAGE, AppConstants.STUDENT_DEACTIVE);
//					return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
//				}
//					
//			}
//
//			else {
//				response.put(AppConstants.MESSAGE, AppConstants.INVALID_CREDENTIALS);
//				return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
//			}
//			
//		}
//
//		else {
//			response.put(AppConstants.MESSAGE, AppConstants.INVALID_DEVICE_TYPE);
//			return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
//		}
//		
//	}

	@Override
	public ResponseEntity<?> registerStudent(Student student) {

		Optional<Student> findByEmailAndMobile = studRepo.findByEmailAndMobile(student.getEmail().trim(),
				student.getMobile().trim());
		if (!findByEmailAndMobile.isPresent()) {
			Optional<Course> course = courseRepository.findByCourseId(student.getCourse().getCourseId());
			student.setCourse(course.get());
			student.setApplyForCourse(course.get().getCourseName());
			Student student1 = studRepo.save(student);
			student1.setPassword(passwordEncoder.encode("123456"));
			student1.setContactFather(student.getContactFather());
			student1.setRole(Roles.STUDENT.toString());
			student1.setUserId(student1.getFullName().split(" ")[0] + "@" + student1.getStudentId());
			student1.setProfilePic(AppConstants.DEFAULT_USER_IMAGE);
			student1.setDeviceId("");
			student1.setInUseDeviceId("");
			student1.setCreatedDate(LocalDateTime.now());
			Student save = studRepo.save(student1);
			StudentReponseForWeb studentFilter = studentFilter(save);
			return new ResponseEntity<>(studentFilter, HttpStatus.OK);

		}
		throw new ResourceAlreadyExistException("Student is Already Exist");
	}

	@Override
	public ResponseEntity<?> login(String userId, String password, String fcmId, String deviceId, String deviceType) {

		Map<String, Object> response = new HashMap<>();
		if (deviceType.equals(ANDROID) || deviceType.equals(IOS)) {
			Student studentByUserId = getStudentByUserId(userId);
			Student studentByInUseDeviceId = getStudentByInUseDeviceId(deviceId);
			StudentLoginResponse studentResponse = new StudentLoginResponse();

			if (studentByUserId != null && encoder.matches(password, studentByUserId.getPassword())) {
				if (studentByUserId.getIsActive()) {
					if (studentByInUseDeviceId == null) {// device Id is not present in db
						// login
						if (studentByUserId.getInUseDeviceId().equals("")) {
							System.out.println("1 CASE");
							studentByUserId.setInUseDeviceId(deviceId);
							studentByUserId.setFcmId(fcmId);
							studentByUserId.setDeviceType(deviceType);
							studentByUserId.setIsDeviceApproved("Approved");
							Student student = studRepo.save(studentByUserId);
							studentResponse.setStudentData(student);

							String token = util.generateTokenForStudent(studentByUserId.getStudentId().toString(),
									studentByUserId.getUserId(), deviceId, Roles.STUDENT.toString());

							studentResponse.setToken(token);
							response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
							response.put("student", studentResponse);
							return new ResponseEntity<>(response, HttpStatus.OK);
						} else {
							System.out.println("2 CASE");
							studentResponse.setIsDeviceIdDifferent(true);
							response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
							response.put("student", studentResponse);

							return new ResponseEntity<>(response, HttpStatus.OK);
						}

					} else {
						if (studentByUserId.getUserId().equals(studentByInUseDeviceId.getUserId())) {
							System.out.println("3 CASE");
							String token = util.generateTokenForStudent(studentByUserId.getStudentId().toString(),
									studentByUserId.getUserId(), deviceId, Roles.STUDENT.toString());
							studentResponse.setToken(token);
							studentResponse.setStudentData(studentByInUseDeviceId);
							response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
							response.put("student", studentResponse);

							return new ResponseEntity<>(response, HttpStatus.OK);
						} else {
							System.out.println("4 CASE");
							studentResponse.setIsDeviceAlreadyInUse(true);
							response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
							response.put("student", studentResponse);
							return new ResponseEntity<>(response, HttpStatus.OK);

						}

					}

				} else {
					response.put(AppConstants.MESSAGE, AppConstants.STUDENT_DEACTIVE);
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}

			} else {
				response.put(AppConstants.MESSAGE, AppConstants.INVALID_CREDENTIALS);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

		}

		response.put(AppConstants.MESSAGE, AppConstants.INVALID_DEVICE_TYPE);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<ApiResponse> approveDevice(String userId, String deviceId) {
		if (Objects.nonNull(userId) && Objects.nonNull(deviceId)) {
			Student findByUserId = studRepo.findByUserId(userId);

			if (findByUserId != null) {
				findByUserId.setDeviceId(deviceId);
				findByUserId.setIsDeviceApproved("Not Approved");
				Student updateStudent = studRepo.save(findByUserId);

				if (updateStudent != null) {

					return new ResponseEntity<>(
							new ApiResponse(Boolean.TRUE, AppConstants.APPROVAL_REQUEST, HttpStatus.OK), HttpStatus.OK);
				}

				else {
					return new ResponseEntity<>(new ApiResponse(Boolean.FALSE, AppConstants.FAILED, HttpStatus.OK),
							HttpStatus.OK);
				}
			}

			else {
				return new ResponseEntity<>(
						new ApiResponse(Boolean.FALSE, AppConstants.INVALID_CREDENTIALS, HttpStatus.BAD_REQUEST),
						HttpStatus.BAD_REQUEST);
			}

		}

		else {
			return new ResponseEntity<>(
					new ApiResponse(Boolean.FALSE, AppConstants.ALL_REQUIRED, HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> approveStudentDevice(String userId, String deviceId) {
		if (Objects.nonNull(userId) && Objects.nonNull(deviceId)) {
			Student student = studRepo.findByUserId(userId);

			if (student != null) {
				student.setInUseDeviceId(deviceId);
				student.setDeviceId("");
				student.setIsDeviceApproved("Approved");

				Student updateStudent = studRepo.save(student);

				if (updateStudent != null) {
					return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, AppConstants.SUCCESS, HttpStatus.OK),
							HttpStatus.OK);
				}

				else {
					return new ResponseEntity<>(new ApiResponse(Boolean.FALSE, AppConstants.FAILED, HttpStatus.OK),
							HttpStatus.OK);
				}
			}

			else {
				return new ResponseEntity<>(
						new ApiResponse(Boolean.FALSE, AppConstants.INVALID_CREDENTIALS, HttpStatus.BAD_REQUEST),
						HttpStatus.BAD_REQUEST);
			}

		}

		else {
			return new ResponseEntity<>(
					new ApiResponse(Boolean.FALSE, AppConstants.ALL_REQUIRED, HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> checkInCheckOut(String latitude, String longitude, String time, String type, String date,
			MultipartFile studentImage, MultipartFile attachment, String workReport, HttpHeaders header) {
		Map<String, Object> response = new HashMap<>();
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Student student = studRepo.findByUserIdAndIsActive(username, true).get();
		boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());
		if (validateToken) {
			if (latitude != null && longitude != null && time != null && date != null && type != null && !type.isEmpty()
					&& studentImage != null && studentImage.getOriginalFilename() != null) {
				Integer studentId = Integer.parseInt(util
						.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());
				Attendance attendanceData = attendenceRepository.findByStudentIdAndCheckInDate(studentId,
						LocalDate.parse(date));
				if (type.equals(AppConstants.CHECK_IN)) {

					if (Objects.isNull(attendanceData)) {

						Attendance checkInAttenedanceData = new Attendance();
						checkInAttenedanceData.setStudentId(studentId);
						checkInAttenedanceData.setCheckInDate(LocalDate.now());
						checkInAttenedanceData.setCheckInTime(LocalTime.now());
						checkInAttenedanceData.setCheckInLat(latitude);
						checkInAttenedanceData.setCheckInLong(longitude);
//						String savePath = helperService.saveImage(studentImage, IMG_UPLOAD_DIR);
						String imageName = fileService.uploadFileInFolder(studentImage, AppConstants.ATTENDANCE_IMAGES);
						checkInAttenedanceData.setCheckInImage(imageName);
						checkInAttenedanceData.setCreatedDate(LocalDateTime.now());
						checkInAttenedanceData.setUpdatedDate(LocalDateTime.now());
						checkInAttenedanceData.setCheckOutStatus("Pending");
						Attendance saveAttendenceCheckInData = attendenceRepository.save(checkInAttenedanceData);
						if (saveAttendenceCheckInData != null) {
							// refresh the seatNumber by 0 to all seats
							// studentSeatingAlloatmentRepo.updateSeatNumber(LocalDate.now());

							List<Integer> allocatedSeats = new ArrayList<>();
							List<StudentSeatingAlloatment> obj = studentSeatingAlloatmentRepo.findAll(LocalDate.now());
							if (obj.size() > 0) {
								for (StudentSeatingAlloatment ran : obj) {
									allocatedSeats.add(ran.getSeatNumber());
								}
							}
							int minRange = 1;
							int maxRange = 100;
							Random random = new Random();
							int seatId = 0;

							while (true) {
								seatId = random.nextInt(maxRange - minRange + 1) + minRange;
								if (!allocatedSeats.contains(seatId)) {
									break;
								}
							}
							Optional<StudentSeatingAlloatment> obj1 = studentSeatingAlloatmentRepo
									.findByStudentId(studentId);

							if (obj1.isPresent()) {
								studentSeatingAlloatmentRepo.updateSeatNumber(studentId, seatId);
							} else {
								StudentSeatingAlloatment obj2 = new StudentSeatingAlloatment();
								obj2.setSeatNumber(seatId);
								obj2.setStudent(student);
								obj2.setSeatAllocatedDate(LocalDate.now());
								studentSeatingAlloatmentRepo.save(obj2);
							}
							// QrManage qrManage = qrManageRepository.findByUserId(username);
							// qrService.jobEnd(qrManage.getUuid(), AppConstants.CHECK_IN);
							response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
							return new ResponseEntity<>(response, HttpStatus.OK);
						} else {
							response.put(AppConstants.MESSAGE, AppConstants.FAILED);
							return new ResponseEntity<>(response, HttpStatus.OK);
						}
					} else {
						response.put(AppConstants.MESSAGE, AppConstants.ALREADY_CHECKIN);
						return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
					}
				} else if (type.equals(AppConstants.CHECK_OUT)) {
					if (workReport != null) {
						if (attendanceData != null) {
							OrganizationInfo organizationInfo = organizationInfoRepository.findById(1).get();
							LocalDateTime checkInDateTime = attendanceData.getCreatedDate();
							LocalDateTime checkOutDateTime = LocalDateTime.now();
							Duration duration = Duration.between(checkInDateTime, checkOutDateTime);
							long workingHours = duration.getSeconds();
							System.out.println(workingHours);
							if (workingHours >= (Long.parseLong(organizationInfo.getWorkingHours()) * 3600)) {
								attendanceData.setCheckOutDate(LocalDate.now());
								attendanceData.setCheckOutTime(LocalTime.now());
								attendanceData.setCheckOutLat(latitude);
								attendanceData.setCheckOutLong(longitude);
								String imageName = fileService.uploadFileInFolder(studentImage,
										AppConstants.ATTENDANCE_IMAGES);
								attendanceData.setCheckOutImage(imageName);
								attendanceData.setWorkingHour(workingHours);
								attendanceData.setCheckOutStatus("Approved");
								attendanceData.setUpdatedDate(LocalDateTime.now());

								Attendance saveAttendenceCheckOutData = attendenceRepository.save(attendanceData);

								StudentWorkReport studentWorkReport = new StudentWorkReport();
								studentWorkReport.setAttendanceId(saveAttendenceCheckOutData.getAttendanceId());
								if (Objects.nonNull(attachment) && (!attachment.getOriginalFilename().equals(""))) {
									String workImageName = fileService.uploadFileInFolder(attachment,
											AppConstants.ATTENDANCE_IMAGES);
									studentWorkReport.setAttachment(workImageName);
								}
								studentWorkReport.setWorkReport(workReport);
								studentWorkReport.setCreatedDate(LocalDateTime.now());
								StudentWorkReport findByAttendanceIdWorkReport = workReportRepository
										.findByAttendanceId(attendanceData.getAttendanceId());
								if (Objects.isNull(findByAttendanceIdWorkReport)) {
									StudentWorkReport workReportData = workReportRepository.save(studentWorkReport);
								}
								if (Objects.nonNull(saveAttendenceCheckOutData)) {
									// QrManage qrManage = qrManageRepository.findByUserId(username);
									// qrService.jobEnd(qrManage.getUuid(), AppConstants.CHECK_OUT);
									response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
									return new ResponseEntity<>(response, HttpStatus.OK);
								} else {
									response.put(AppConstants.MESSAGE, AppConstants.FAILED);
									return new ResponseEntity<>(response, HttpStatus.OK);
								}
							} else {
								response.put(AppConstants.MESSAGE, AppConstants.EARLY_CHECKOUT);
								return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
							}
						} else {
							response.put(AppConstants.MESSAGE, AppConstants.NOT_CHECKED_IN);
							return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
						}
					} else {
						response.put(AppConstants.MESSAGE, AppConstants.ALL_REQUIRED);
						return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
					}
				}
			} else

			{
				response.put(AppConstants.MESSAGE, AppConstants.ALL_REQUIRED);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

			}
		}

		response.put(AppConstants.MESSAGE, AppConstants.UNAUTHORIZED);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@Override
	public ResponseEntity<?> dashboard(HttpHeaders header) {
		Map<String, Object> response = new HashMap<String, Object>();

		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Integer studentId = Integer.parseInt(
				util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());
		Student student = studRepo.findByUserIdAndIsActive(username, true).get();

		StudentResponse studentResponseDto = new StudentResponse();
		DashboardResponse dashboardResponseDto = new DashboardResponse();
		MispunchResponse mispunchResponseDto = new MispunchResponse();

		Boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());

		if (validateToken) {
			Attendance attendance = new Attendance();

			attendance = attendenceRepository.findByStudentIdAndCheckInDate(studentId, LocalDate.now());
			if (attendance != null) {
				if (attendance.getCheckOutDate() == null) {
					dashboardResponseDto.setAttendanceStatus(AppConstants.CHECK_IN);
					dashboardResponseDto.setCheckInDate(attendance.getCheckInDate());
					dashboardResponseDto.setCheckInTime(attendance.getCheckInTime());
					dashboardResponseDto.setCheckInImage(attendance.getCheckInImage());

				} else {
					dashboardResponseDto.setAttendanceStatus(AppConstants.CHECK_OUT);
					dashboardResponseDto.setCheckInDate(attendance.getCheckInDate());
					dashboardResponseDto.setCheckInTime(attendance.getCheckInTime());
					dashboardResponseDto.setCheckInImage(attendance.getCheckInImage());
					dashboardResponseDto.setCheckOutImage(attendance.getCheckOutImage());
					dashboardResponseDto.setCheckOutTime(attendance.getCheckOutTime());
					dashboardResponseDto.setCheckOutDate(attendance.getCheckOutDate());
				}
			}

			else
				dashboardResponseDto.setAttendanceStatus(AppConstants.CHECK_OUT);

//			dashboardResponseDto.setIsFeesDue(false);
//			dashboardResponseDto.setFeesDueDate(null);

			if (student != null) {
				studentResponseDto.setUserId(student.getUserId());
				studentResponseDto.setStudentId(student.getStudentId());
				studentResponseDto.setFullName(student.getFullName());
				studentResponseDto.setMobile(student.getMobile());
				studentResponseDto.setEmail(student.getEmail());
				studentResponseDto.setDob(student.getDob());

				studentResponseDto.setProfilePic(student.getProfilePic());
			}

			dashboardResponseDto.setStudentResponseDto(studentResponseDto);
			dashboardResponseDto.setOrganizationInfo(organizationInfoRepository.findById(1).get());

			Attendance attendance2 = checkStudentMispunch(studentId);

			if (attendance2 != null) {
				dashboardResponseDto.setIsMispunch(true);
				mispunchResponseDto.setMispunchStatus(attendance2.getCheckOutStatus());
				mispunchResponseDto.setMispunchDate(attendance2.getCheckInDate());
				mispunchResponseDto.setCheckInTime(attendance2.getCheckInTime());
				dashboardResponseDto.setMispunchResponseDto(mispunchResponseDto);
			}

			else
				dashboardResponseDto.setIsMispunch(false);

			if (Objects.nonNull(dashboardResponseDto)) {

				response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
				QrManage findByUserId = qrManageRepository.findByUserId(username);
				if (Objects.nonNull(findByUserId))
					dashboardResponseDto.setIsWebLoggedIn(true);

				Optional<StudentSeatingAlloatment> obj = studentSeatingAlloatmentRepo.findByStudentIdAndDate(studentId,
						LocalDate.now());
				if (obj.isEmpty()) {
					Optional<StudentSeatingAlloatment> obj3 = studentSeatingAlloatmentRepo.findByStudentId(studentId);
					if (obj3.isPresent()) {
						obj3.get().setSeatNumber(0);
						studentSeatingAlloatmentRepo.save(obj3.get());
					}
				}
				if (obj.isPresent())
					dashboardResponseDto.setSeatNumber(obj.get().getSeatNumber());

				AttendenceOfMonth res = currentMonthAttendenceForDashBoard(studentId, "CURRENT_MONTH");
				dashboardResponseDto.setTotalPresent(res.getTotalPresent());
				dashboardResponseDto.setTotalAbsent(res.getTotalAbsent());
				dashboardResponseDto.setTotalEarlyCheckOut(res.getTotalEarlyCheckOut());
				dashboardResponseDto.setTotalMispunch(res.getTotalMispunch());
				MockResponse mock = checkMockForStudent(studentId);
				dashboardResponseDto.setMock(mock.getIsMock());
				dashboardResponseDto.setMockDate(mock.getMockDate());
				dashboardResponseDto.setMockPerson(mock.getMockPerson());
				CounsellingResponse counselling = checkCounsellingForStudent(studentId);
				dashboardResponseDto.setCounselling(counselling.getIsCounselling());
				dashboardResponseDto.setCounsellingDate(counselling.getCounsellingDate());
				dashboardResponseDto.setCounsellingPerson(counselling.getCounsellingPerson());

				response.put("dashboardResponseDto", dashboardResponseDto);
				return new ResponseEntity<>(response, HttpStatus.OK);

			} else {
				response.put(AppConstants.MESSAGE, AppConstants.FAILED);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

		}

		response.put(AppConstants.MESSAGE, AppConstants.UNAUTHORIZED);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	private Attendance checkStudentMispunch(Integer studentId) {

		return attendenceRepository.findByStudentIdAndCheckInDateLessThanCurrentDate(studentId, LocalDate.now());
	}

	@Override
	public ResponseEntity<?> studentMispunchRequest(HttpHeaders header, String time, String date, String workReport,
			MultipartFile attachment) {
		Map<String, Object> response = new HashMap<>();
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Student student = studRepo.findByUserIdAndIsActive(username, true).get();
		Boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());
		if (validateToken) {

			Integer studentId = Integer.parseInt(
					util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());
			if (time != null && date != null && workReport != null) {
				Attendance attendanceData = attendenceRepository.findByStudentIdAndCheckInDate(studentId,
						LocalDate.parse(date));

				if (attendanceData != null && Objects.nonNull(attendanceData.getCheckOutStatus().equals("Pending"))) {
					LocalDateTime checkInDateTime = attendanceData.getCreatedDate();
					LocalDateTime checkOutDateTime = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time));
					Duration duration = Duration.between(checkInDateTime, checkOutDateTime);
					long workingHour = duration.getSeconds();
					attendanceData.setCheckOutDate(LocalDate.parse(date));
					attendanceData.setCheckOutTime(LocalTime.parse(time));
					attendanceData.setWorkingHour(workingHour);
					attendanceData.setCheckOutStatus("Approved");
					attendanceData.setUpdatedDate(checkOutDateTime);
					attendanceData.setIsMispunch(true);
					Attendance saveAttdance = attendenceRepository.save(attendanceData);

					StudentWorkReport studentWorkReport = new StudentWorkReport();
					studentWorkReport.setAttendanceId(saveAttdance.getAttendanceId());
					if (Objects.nonNull(attachment) && (!attachment.getOriginalFilename().equals(""))) {
						String workImageName = fileService.uploadFileInFolder(attachment,
								AppConstants.ATTENDANCE_IMAGES);
						studentWorkReport.setAttachment(workImageName);
					}
					studentWorkReport.setWorkReport(workReport);
					studentWorkReport.setCreatedDate(LocalDateTime.now());
					StudentWorkReport findByAttendanceIdWorkReport = workReportRepository
							.findByAttendanceId(attendanceData.getAttendanceId());
					if (Objects.isNull(findByAttendanceIdWorkReport)) {
						StudentWorkReport workReportData = workReportRepository.save(studentWorkReport);
					}
					if (Objects.nonNull(saveAttdance)) {
						response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
						return new ResponseEntity<>(response, HttpStatus.OK);

					} else {
						response.put(AppConstants.MESSAGE, AppConstants.FAILED);
						return new ResponseEntity<>(response, HttpStatus.OK);
					}

				} else {
					response.put(AppConstants.MESSAGE, AppConstants.NOT_CHECKED_IN);
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
			} else {
				response.put(AppConstants.MESSAGE, AppConstants.ALL_REQUIRED);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

			}
		}

		response.put(AppConstants.MESSAGE, AppConstants.UNAUTHORIZED);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

	}

	@Override
	public ResponseEntity<?> getStudentProfileApi(HttpHeaders header) {
		Map<String, Object> response = new HashMap<>();

		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));

		Student student = studRepo.findByUserIdAndIsActive(username, true).get();

		StudentResponse studentResponseDto = new StudentResponse();
		DashboardResponse dashboardResponseDto = new DashboardResponse();

		Boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());

		if (validateToken) {

			Integer studentId = Integer.parseInt(
					util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());
			LocalDate currentDate = LocalDate.now();
			Student findByStudentId = studRepo.findByStudentId(studentId);
			if (findByStudentId != null) {
				studentResponseDto.setFullName(student.getFullName());
				studentResponseDto.setMobile(student.getMobile());
				studentResponseDto.setEmail(student.getEmail());
				studentResponseDto.setDob(student.getDob());
				studentResponseDto.setApplyForCourse(student.getApplyForCourse());
				studentResponseDto.setJoinDate(student.getJoinDate());

				if (studentResponseDto.getCompletionDuration() == false) {
					long monthsDifference = HelperService.getMonthsDifference(student.getJoinDate(), currentDate);

					if (monthsDifference >= 9) {
						studentResponseDto.setCompletionDuration(true);

					}

				}

				studentResponseDto.setProfilePic(student.getProfilePic());
//				File f = new File(IMG_UPLOAD_DIR + student.getProfilePic());
//                
//				if((student.getProfilePic()!=null) && (f.exists())) {
//			
//					studentResponseDto.setProfilePic(student.getProfilePic());
//				}
			}
			dashboardResponseDto.setStudentResponseDto(studentResponseDto);
			dashboardResponseDto.setOrganizationInfo(organizationInfoRepository.findById(1).get());

			AttendenceOfMonth res = currentMonthAttendenceForDashBoard(studentId, "CURRENT_YEAR");
			dashboardResponseDto.setTotalPresent(res.getTotalPresent());
			dashboardResponseDto.setTotalAbsent(res.getTotalAbsent());
			dashboardResponseDto.setTotalEarlyCheckOut(res.getTotalEarlyCheckOut());
			dashboardResponseDto.setTotalMispunch(res.getTotalMispunch());
			if (Objects.nonNull(dashboardResponseDto)) {
				response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
				QrManage findByUserId = qrManageRepository.findByUserId(username);
				if (Objects.nonNull(findByUserId))
					dashboardResponseDto.setIsWebLoggedIn(true);
				response.put("dashboardResponseDto", dashboardResponseDto);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put(AppConstants.MESSAGE, AppConstants.FAILED);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

		}

		response.put(AppConstants.MESSAGE, AppConstants.UNAUTHORIZED);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@Override
	public ResponseEntity<?> studentEarlyCheckoutRequest(HttpHeaders header, String latitude, String longitude,
			String time, String date, String type, String workReport, MultipartFile studentImage,
			MultipartFile attachment) {
		Map<String, Object> response = new HashMap<>();

		CheckoutResponse checkoutResponseDto = new CheckoutResponse();
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Integer studentId = Integer.parseInt(
				util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());
		Student student = studRepo.findByUserIdAndIsActive(username, true).get();
		Boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());

		if (validateToken) {

			if (latitude != null && longitude != null && time != null && date != null && type != null
					&& studentImage != null && studentImage.getOriginalFilename() != null && workReport != null) {
				Attendance attendance = attendenceRepository.findByStudentIdAndCheckInDateAndCheckOutDate(studentId,
						LocalDate.parse(date), null);

				if (attendance != null) {

					LocalDateTime checkInDateTime = attendance.getCreatedDate();
					LocalDateTime checkOutDateTime = LocalDateTime.now();
					Duration duration = Duration.between(checkInDateTime, checkOutDateTime);
					Long workingHours = duration.getSeconds();

					checkoutResponseDto.setCheckoutDate(LocalDate.now());
					checkoutResponseDto.setCheckoutTime(LocalTime.now());
					checkoutResponseDto.setCheckoutLat(latitude);
					checkoutResponseDto.setCheckoutLong(longitude);

//					String savePath = helperService.saveImage(studentImage, IMG_UPLOAD_DIR);
					String imageName = fileService.uploadFileInFolder(studentImage, AppConstants.ATTENDANCE_IMAGES);
					checkoutResponseDto.setCheckoutImage(imageName);

					checkoutResponseDto.setWorkingHour(workingHours);
					checkoutResponseDto.setCheckoutImage(imageName);
					checkoutResponseDto.setCheckoutStatus("Approved");
					checkoutResponseDto.setUpdatedDate(LocalDateTime.now());

					attendance.setCheckOutDate(checkoutResponseDto.getCheckoutDate());
					attendance.setCheckOutTime(checkoutResponseDto.getCheckoutTime());
					attendance.setCheckOutLat(checkoutResponseDto.getCheckoutLat());
					attendance.setCheckOutLong(checkoutResponseDto.getCheckoutLong());
					attendance.setCheckOutImage(checkoutResponseDto.getCheckoutImage());
					attendance.setWorkingHour(checkoutResponseDto.getWorkingHour());
					attendance.setCheckOutStatus(checkoutResponseDto.getCheckoutStatus());
					attendance.setUpdatedDate(checkoutResponseDto.getUpdatedDate());

					Attendance updateAttendance = attendenceRepository.save(attendance);
					StudentWorkReport studentWorkReport = new StudentWorkReport(0, attendance.getAttendanceId(),
							workReport, LocalDateTime.now());
					if (Objects.nonNull(attachment) && (!attachment.getOriginalFilename().equals(""))) {
						String workImageName = fileService.uploadFileInFolder(attachment,
								AppConstants.ATTENDANCE_IMAGES);
						studentWorkReport.setAttachment(workImageName);
					}

					StudentWorkReport findByAttendanceIdWorkReport = workReportRepository
							.findByAttendanceId(attendance.getAttendanceId());
					if (Objects.isNull(findByAttendanceIdWorkReport)) {
						workReportRepository.save(studentWorkReport);
					}

					if (updateAttendance != null) {
						response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
						return new ResponseEntity<>(response, HttpStatus.OK);
					} else {
						response.put(AppConstants.MESSAGE, AppConstants.FAILED);
						return new ResponseEntity<>(response, HttpStatus.OK);
					}
				} else {
					response.put(AppConstants.MESSAGE, AppConstants.NOT_CHECKED_IN);
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
			} else {
				response.put(AppConstants.MESSAGE, AppConstants.ALL_REQUIRED);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		}
		response.put(AppConstants.MESSAGE, AppConstants.UNAUTHORIZED);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	// get history data after checkout
	@Override
	public ResponseEntity<?> getStudentCheckInCheckOutHistory(HttpHeaders header, String startDate, String endDate,
			Integer offset, Integer limit) {
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Integer studentId = Integer.parseInt(
				util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());

		boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), username);
		Map<String, Object> response = new HashMap<>();

		if (validateToken) {
			List<Attendance> attendanceHistory = attendenceRepository.findAttendanceHistory(studentId,
					LocalDate.parse(startDate), LocalDate.parse(endDate), offset, limit);
			Page<Attendance> pageData = attendenceRepository.findAttendanceHistory(studentId,
					LocalDate.parse(startDate), LocalDate.parse(endDate), PageRequest.of(0, 10));
			List<CheckinCheckoutHistoryResponse> historyDto = new ArrayList<>();
			if (!attendanceHistory.isEmpty()) {
				// List<Attendance> content = attendanceHistory.getContent();
				for (Attendance attendance : attendanceHistory) {
					StudentWorkReport stdWorkReport = workReportRepository
							.findByAttendanceId(attendance.getAttendanceId());
					CheckinCheckoutHistoryResponse cicoHistoryObjDto = getCicoHistoryObjDto(attendance);
					if (stdWorkReport != null) {
						cicoHistoryObjDto.setWorkReport(stdWorkReport.getWorkReport());
						cicoHistoryObjDto.setAttachment(stdWorkReport.getAttachment());
					}
					historyDto.add(cicoHistoryObjDto);
				}

				Map<String, Object> map = new HashMap<>();
				map.put("attendance", historyDto);
//				map.put("totalPages", attendanceHistory.getTotalPages());
				map.put("totalAttendance", pageData.getTotalElements());
//				map.put("currentPage", attendanceHistory.getNumber());
//				map.put("pageSize", attendanceHistory.getNumberOfElements());
				response.put("response", map);
				response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put(AppConstants.MESSAGE, AppConstants.NO_DATA_FOUND);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		}
		response.put(AppConstants.MESSAGE, AppConstants.UNAUTHORIZED);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	private CheckinCheckoutHistoryResponse getCicoHistoryObjDto(Attendance attendance) {
		CheckinCheckoutHistoryResponse historyDto = new CheckinCheckoutHistoryResponse();
		historyDto.setAttendanceId(attendance.getAttendanceId());
		historyDto.setCheckInDate(attendance.getCheckInDate());
		historyDto.setCheckInImage(attendance.getCheckInImage());
		historyDto.setCheckInTime(attendance.getCheckInTime());
		historyDto.setCheckOutDate(attendance.getCheckOutDate());
		historyDto.setCheckOutImage(attendance.getCheckOutImage());
		historyDto.setCheckOutTime(attendance.getCheckOutTime());
		historyDto.setWorkingHour(attendance.getWorkingHour());

		return historyDto;
	}

	@Override
	public ResponseEntity<?> studentChangePassword(HttpHeaders header, String oldPassword, String newPassword) {
		Map<String, Object> response = new HashMap<>();
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Student student = studRepo.findByUserIdAndIsActive(username, true).get();
		boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());
		if (validateToken) {
			if (Objects.nonNull(oldPassword) && Objects.nonNull(newPassword)) {
				if (encoder.matches(oldPassword, student.getPassword())) {
					Boolean checkPasswordValidation = true;
					if (checkPasswordValidation) {
						student.setPassword(encoder.encode(newPassword));
						Student updatedStudent = studRepo.save(student);
						if (updatedStudent != null) {
							response.put(AppConstants.MESSAGE, AppConstants.PASSWORD_CHANGED);
							return new ResponseEntity<>(response, HttpStatus.OK);
						} else {
							response.put(AppConstants.MESSAGE, AppConstants.PASSWORD_NOT_CHANGED);
							return new ResponseEntity<>(response, HttpStatus.OK);
						}
					} else {
						response.put(AppConstants.MESSAGE, AppConstants.INVALID_PASSWORD_FORMAT);
						return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
					}
				} else {
					response.put(AppConstants.MESSAGE, AppConstants.WRONG_PASSWORD);
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
			} else {
				response.put(AppConstants.MESSAGE, AppConstants.ALL_REQUIRED);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		}

		response.put(AppConstants.MESSAGE, AppConstants.UNAUTHORIZED);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@Override
	public ResponseEntity<?> updateStudentProfile(HttpHeaders header, String fullName, String mobile, String dob,
			String email, MultipartFile profilePic) {
		Map<String, Object> response = new HashMap<>();
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Student student = studRepo.findByUserIdAndIsActive(username, true).get();
		boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());
		if (validateToken) {
			if (!fullName.equals("")) {
				student.setFullName(fullName);
			}
			if (!dob.equals("")) {
				student.setDob(LocalDate.parse(dob));
			}
			if (!mobile.equals("")) {
				student.setMobile(mobile);
			}
			if (!email.equals("")) {
				student.setEmail(email);
			}
			if (!(profilePic.isEmpty()) && !(profilePic.getOriginalFilename().equals(""))) {
				String imageName = fileService.uploadFileInFolder(profilePic, AppConstants.ATTENDANCE_IMAGES);
				student.setProfilePic(imageName);
			}

			if (Objects.nonNull(student)) {
				Student updatedStudent = studRepo.save(student);
				if (updatedStudent != null) {
					response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {
					response.put(AppConstants.MESSAGE, AppConstants.FAILED);
					return new ResponseEntity<>(response, HttpStatus.OK);
				}
			} else {
				response.put(AppConstants.MESSAGE, AppConstants.FAILED);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		}

		response.put(AppConstants.MESSAGE, AppConstants.UNAUTHORIZED);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@Override
	public Map<String, Object> getTodayAttendance(Integer studentId) {
		Map<String, Object> map = new HashMap<>();
		Attendance attendance = attendenceRepository.findByStudentIdAndCheckInDate(studentId, LocalDate.now());
		// String format =
		// attendance.getCheckInTime().format(DateTimeFormatter.ofPattern("hh:mm:ss"));
		if (Objects.nonNull(attendance)) {
			map.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
			map.put("Attendance", attendance);
		} else {
			map.put(AppConstants.MESSAGE, AppConstants.NOT_CHECKED_IN);

		}
		return map;
	}

	@Override
	public Map<String, Object> studentAttendanceMonthFilter(HttpHeaders header, Integer monthNo) {
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Integer studentId = Integer.parseInt(
				util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());
		Student student = studRepo.findByUserIdAndIsActive(username, true).get();
		Boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());

		Map<String, Object> map = new HashMap<>();

		if (validateToken) {
			List<Attendance> findByStudentIdAndMonthNo = attendenceRepository.findByStudentIdAndMonthNo(studentId,
					monthNo);
			Collections.reverse(findByStudentIdAndMonthNo);
			if (!findByStudentIdAndMonthNo.isEmpty()) {
				map.put(AppConstants.MESSAGE, AppConstants.SUCCESS);

				map.put("AttendanceData", findByStudentIdAndMonthNo);
			} else {
				map.put(AppConstants.MESSAGE, AppConstants.NO_DATA_FOUND);

				map.put("AttendanceData", findByStudentIdAndMonthNo);
			}
		} else {
			map.put(AppConstants.MESSAGE, AppConstants.UNAUTHORIZED);

		}
		return map;
	}

	@SuppressWarnings("unlikely-arg-type")
	public Map<String, Object> getCalenderData(Integer id, Integer month, Integer year) { // working code
		Map<String, Object> response = new HashMap<>();
		LocalDate joinDate = studRepo.findById(id).get().getJoinDate();
		if (year >= joinDate.getYear() && year <= LocalDate.now().getYear()) {

			List<Integer> present = new ArrayList<>();
			List<Integer> leaves = new ArrayList<>();
			List<Integer> absent = new ArrayList<>();
			List<Integer> mispunch = new ArrayList<>();
			List<Integer> earlycheckout = new ArrayList<>();

			// Get the first day of the month
			LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);

			YearMonth yearMonth = YearMonth.of(year, month);
			int lastDay = yearMonth.lengthOfMonth();
			LocalDate lastDayOfMonth = LocalDate.of(year, month, lastDay);
			LocalDate currentDay = firstDayOfMonth;
			StudentCalenderResponse data = new StudentCalenderResponse();
			LocalDate currentDate = LocalDate.now();

			if (LocalDate.now().getYear() != year || month <= LocalDate.now().getMonthValue()) {
				// counting total leaves
				List<Leaves> leavesData = leaveRepository.findAllByStudentIdForCurrentMonth(id, month, year);
				for (Leaves list : leavesData) {
					LocalDate startLeaveDate = list.getLeaveDate();
					LocalDate endLeaveDate = list.getLeaveEndDate();

					while (!startLeaveDate.isAfter(endLeaveDate)) {
						leaves.add(startLeaveDate.getDayOfMonth());
						startLeaveDate = startLeaveDate.plusDays(1);
					}
				}

				currentDay = firstDayOfMonth;

				List<Attendance> studentAttendanceList = attendenceRepository.findByStudentIdForCurrentMonth(id, month,
						year);
				for (Attendance attendance : studentAttendanceList) {
					LocalDate attendanceDate = attendance.getCheckInDate();
					present.add(attendanceDate.getDayOfMonth());
				}

				List<Attendance> obj1 = attendenceRepository.countTotalEarlyCheckOutForCurrent1(id, month, year);
				for (Attendance attendance : obj1) {
					LocalDate attendanceDate = attendance.getCheckInDate();
					earlycheckout.add(attendanceDate.getDayOfMonth());
				}

				List<Attendance> obj2 = attendenceRepository.countTotalMishpunchForCurrentYear1(id, month, year);
				for (Attendance attendance : obj2) {
					LocalDate attendanceDate = attendance.getCheckInDate();
					mispunch.add(attendanceDate.getDayOfMonth());
				}

				// getting total absent for current month and till today date
				if (currentDate.getMonthValue() == month && LocalDate.now().getYear() == year) {
					if (month == joinDate.getMonth().getValue() && (year == joinDate.getYear())) {
						currentDay = joinDate;
					}
					while (currentDay.getDayOfMonth() <= currentDate.getDayOfMonth() - 1
							&& !currentDay.isAfter(lastDayOfMonth)) {
						if (!present.contains(currentDay.getDayOfMonth())
								&& !mispunch.contains(currentDay.getDayOfMonth())
								&& !earlycheckout.contains(currentDay.getDayOfMonth())
								&& !leaves.contains(currentDay.getDayOfMonth())
								&& currentDay.getDayOfWeek() != DayOfWeek.SUNDAY) {
							absent.add(currentDay.getDayOfMonth());
						}
						currentDay = currentDay.plusDays(1);
					}
				} else {// getting absent for previous month from current month
					if (month <= joinDate.getMonth().getValue() && (year <= joinDate.getYear())) {
						currentDay = joinDate;
					}
					while (!currentDay.isAfter(lastDayOfMonth)) {
						if (!present.contains(currentDay.getDayOfMonth())
								&& !mispunch.contains(currentDay.getDayOfMonth())
								&& !earlycheckout.contains(currentDay.getDayOfMonth())
								&& !leaves.contains(currentDay.getDayOfMonth())
								&& currentDay.getDayOfWeek() != DayOfWeek.SUNDAY) {
							absent.add(currentDay.getDayOfMonth());
						}
						currentDay = currentDay.plusDays(1);
					}
				}
			}
			data.setPresent(present);
			data.setAbsent(absent);
			data.setLeaves(leaves);
			data.setMispunch(mispunch);
			data.setEarlyCheckOut(earlycheckout);

			response.put("StudentCalenderData", data);
			response.put("status", true);
		} else {

			response.put("status", false);
		}
		return response;
	}

	@Override
	public Map<String, Object> getStudentData(Integer studentId) {
		Student student = studRepo.findById(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found with this id :"));
		Map<String, Object> response = new HashMap<>();
		response.put("studentName", student.getFullName());
		response.put("profilePic", student.getProfilePic());
		response.put("course", student.getApplyForCourse());
		response.put("id", student.getStudentId());
		return response;
	}

	@Override
	public ResponseEntity<?> getTotalTodayAbsentStudent(Integer pageNumber, Integer pageSize) { // getting present and
																								// absent students

		Page<StudentReponseForWeb> result = studRepo.getTotalTodayAbsentStudent(LocalDate.now(),
				PageRequest.of(pageSize, pageNumber));
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	// here student details depends on this method [ getStudentData(id) ]so ,
	// changes
	// reflect here if any changes are done in that method
	@Override
	public ResponseEntity<?> getTotalStudentInLeaves(Integer pageSize, Integer pageNumber) {
		Page<OnLeavesResponse> totalStudentInLeaves = studRepo
				.getTotalStudentInLeaves(PageRequest.of(pageNumber, pageSize));
		return new ResponseEntity<>(totalStudentInLeaves, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getTotalTodaysLeavesRequest(Integer pageSize, Integer pageNumber) {
		Page<TodayLeavesRequestResponse> totalTodaysLeavesRequest = studRepo
				.getTotalTodaysLeavesRequest(PageRequest.of(pageNumber, pageSize));
		return new ResponseEntity<>(totalTodaysLeavesRequest, HttpStatus.OK);
	}

	@Override
	public Boolean approveStudentLeaveReqeust(Integer studentId, Integer leaveId, String leaveStatus) {

		int updateStudentLeaves = 0;
		if (leaveStatus.equals("approve")) {
			updateStudentLeaves = leaveRepository.updateStudentLeaves(studentId, 1, leaveId);
		} else if (leaveStatus.equals("deny")) {
			updateStudentLeaves = leaveRepository.updateStudentLeaves(studentId, 2, leaveId);
		}

		// .....firebase notification .....//
		NotificationInfo fcmIds = studRepo.findFcmIdByStudentId(studentId);

		String message = String.format(
				leaveStatus.equals("approve") ? " %s your leave request has been approved. Enjoy your time off!"
						: "%s your leave request has been denied. Reach out to the admin if you have any questions.",
				fcmIds.getFullName());
		fcmIds.setMessage(message);
		fcmIds.setTitle(leaveStatus.equals("approve") ? "Leave Approved!" : "Leave Request Denied!");

		kafkaProducerService.sendNotification(NotificationConstant.COMMON_TOPIC, fcmIds.toString());
		// .....firebase notification .....//

		return (updateStudentLeaves != 0) ? true : false;
	}

	@Override
	public PageResponse<StudentReponseForWeb> getAllStudentData(Integer page, Integer size) {

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "studentId");
		Page<Object[]> student = studRepo.findAllByIsCompletedAndIsActive(false, true, pageable);
		List<StudentReponseForWeb> reponseForWeb = new ArrayList<>();

		if (student.getNumberOfElements() == 0) {
			return new PageResponse<>(Collections.emptyList(), student.getNumber(), student.getSize(),
					student.getTotalElements(), student.getTotalPages(), student.isLast());
		}

		for (Object[] row : student.getContent()) {

			StudentReponseForWeb std = new StudentReponseForWeb();
			std.setStudentId((Integer) row[0]);
			std.setFullName((String) row[1]);
			std.setMobile((String) row[2]);
			std.setDob((LocalDate) row[3]);
			std.setProfilePic((String) row[4]);
			std.setEmail((String) row[7]);
			std.setJoinDate((LocalDate) row[8]);
			std.setApplyForCourse((String) row[9]);
			std.setFinalFees((Double) row[10]);
			CourseResponse courseResponse = new CourseResponse();
			courseResponse.setCourseName((String) row[5]);
			courseResponse.setCourseId((Integer) row[6]);
			std.setCourseResponse(courseResponse);
			reponseForWeb.add(std);

		}

		return new PageResponse<>(reponseForWeb, student.getNumber(), student.getSize(), student.getTotalElements(),
				student.getTotalPages(), student.isLast());
	}

	@Override
	public PageResponse<StudentReponseForWeb> searchStudentByName(String fullName, Integer pageNumber,
			Integer pageSize) {
		Page<StudentReponseForWeb> res = studRepo.findAllByFullNameContaining(fullName,
				PageRequest.of(pageNumber, pageSize));

		return new PageResponse<>(res.getContent(), res.getNumber(), res.getSize(), res.getTotalElements(),
				res.getTotalPages(), res.isLast());
	}

	@Override
	public StudentResponse getStudentById(Integer studentId) {
		// TODO Auto-generated method stub
		Student student = studRepo.findById(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found from given id"));
		return mapper.map(student, StudentResponse.class);

	}

	@Override
	public ResponseEntity<?> getStudentByIdForWeb(Integer studentId) {
		Student student = studRepo.findById(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found from given id"));
		return new ResponseEntity<>(studentFilter(student), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getStudentProfileForWeb(Integer studentId) {
		Student findByStudentId = studRepo.findByStudentId(studentId);
		if (Objects.isNull(findByStudentId))
			throw new ResourceNotFoundException("Student Not Found");
		StudentResponse studentResponse = mapper.map(findByStudentId, StudentResponse.class);
		return new ResponseEntity<>(studentResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> updateStudent(Student student) {
		Student studentData = studRepo.findByUserIdAndIsActive(student.getUserId(), true).get();
		if (Objects.nonNull(studentData)) {
			if (student.getFullName() != null)
				studentData.setFullName(student.getFullName());
			if (student.getMobile() != null)
				studentData.setMobile(student.getMobile());
			if (student.getMothersName() != null)
				studentData.setMothersName(student.getMothersName());
			if (student.getFathersName() != null)
				studentData.setFathersName(student.getFathersName());
			if (student.getLocalAddress() != null)
				studentData.setLocalAddress(student.getLocalAddress());
			if (student.getParmanentAddress() != null)
				studentData.setParmanentAddress(student.getParmanentAddress());
			if (student.getCurrentCourse() != null)
				studentData.setCurrentCourse(student.getCurrentCourse());
			if (student.getCollege() != null)
				studentData.setCollege(student.getCollege());
			if (student.getApplyForCourse() != null)
				studentData.setApplyForCourse(student.getApplyForCourse());
			if (student.getJoinDate() != null)
				studentData.setJoinDate(student.getJoinDate());
			Student save = studRepo.save(studentData);
			StudentResponse res = mapper.map(save, StudentResponse.class);
			return new ResponseEntity<>(res, HttpStatus.OK);

		} else {
			return new ResponseEntity<>(AppConstants.NO_DATA_FOUND, HttpStatus.BAD_REQUEST);
		}

	}

	@Override
	public ResponseEntity<?> getStudentOverAllAttendanceData(Integer studentId, Integer pageSize, Integer pageNumber) {

		List<AttendanceLogResponse> attendanceList = new ArrayList<>();
		Page<Attendance> findAllByStudentId = attendenceRepository.findAllByStudentId(studentId,
				PageRequest.of(pageNumber, pageSize));
		for (Attendance attendance : findAllByStudentId) {
			AttendanceLogResponse logResponse = new AttendanceLogResponse();
			logResponse.setDate(attendance.getCheckInDate());
			logResponse.setCheckIn(attendance.getCheckInTime());
			logResponse.setCheckOut(attendance.getCheckOutTime());
			logResponse.setTimeIn(attendance.getWorkingHour());
			if (attendance.getWorkingHour() >= TIME_PERIOD_NINE_HOURS) {
				logResponse.setStatus("FullDay");
			} else {
				logResponse.setStatus("HalfDay");
			}
			attendanceList.add(logResponse);
		}

		Page<Leaves> leavesList = leaveRepository.getStudentAllLeavesAndApproved(studentId, 1,
				PageRequest.of(pageNumber, pageSize));
		for (Leaves leaves : leavesList) {
			AttendanceLogResponse logResponse = null;
			LocalDate leavesDate = leaves.getLeaveDate();
			for (int i = 1; i <= leaves.getLeaveDuration(); i++) {
				logResponse = new AttendanceLogResponse();
				logResponse.setDate(leavesDate);
				logResponse.setStatus("OnLeave");
				leavesDate = leavesDate.plusDays(1);
				attendanceList.add(logResponse);
			}
		}

		attendanceList.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
		Page<AttendanceLogResponse> res = new PageImpl<>(attendanceList, PageRequest.of(pageNumber, pageSize),
				findAllByStudentId.getTotalElements() + leavesList.getTotalElements());
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	public static Map<String, Integer> countTotalDaysInMonth(int month) {
		Integer totalDays;
		if (month == (LocalDate.now().getMonthValue()))
			totalDays = LocalDate.now().getDayOfMonth();

		else

		{
			if (month < 1 || month > 12) {
				throw new IllegalArgumentException("Month should be between 1 and 12");
			}

			// Get the current year
			int currentYear = YearMonth.now().getYear();

			// Create a YearMonth object for the given month and current year
			YearMonth yearMonth = YearMonth.of(currentYear, month);

			// Get the total number of days in the month
			totalDays = yearMonth.lengthOfMonth();
		}

		int sundays = 0;

		System.out.println(totalDays);

		for (int day = 1; day <= totalDays; day++) {
			LocalDate date = LocalDate.of(LocalDate.now().getYear(), month, day);
			if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
				sundays++;
			}
		}
		Map<String, Integer> map = new HashMap<>();
		map.put("TotalDays", totalDays - sundays);
		map.put("Sundays", sundays);
		return map;
	}

	@Override
	public ResponseEntity<?> getMonthwiseAttendence(Integer month) {
		Map<String, Long> map = new HashMap<>();

		Long presentStudents = attendenceRepository.countPresentStudentsByMonth(month);
		Long onLeaveStudents = leaveRepository.countLeaveStudentsByMonth(month);
		Long totalStudents = studRepo.countTotalStudents();

		System.out.println(totalStudents);

		Map<String, Integer> map2 = countTotalDaysInMonth(month);
		Integer sundays = map2.get("Sundays");
		Integer totalDays = map2.get("TotalDays");

		Long absentStudents = (totalStudents * totalDays)
				- (presentStudents + onLeaveStudents + (sundays * totalStudents));

		map.put("Present", presentStudents);
		map.put("Absent", absentStudents);
		map.put("OnLeave", onLeaveStudents);

		return new ResponseEntity<>(map, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> getTodaysPresentsAndEarlyCheckouts(String key) {
		System.out.println(key);
		List<StudentPresentAndEarlyCheckOut> studentsPresentAndEarlyCheckout = new ArrayList<>();
		if (key.equals("Present")) {
			List<Object[]> todaysPresents = attendenceRepository.getTodaysPresents(LocalDate.now());
			System.out.println(todaysPresents);
			for (Object[] row : todaysPresents) {
				StudentPresentAndEarlyCheckOut student = new StudentPresentAndEarlyCheckOut();
				student.setFullName((String) row[0]);
				student.setMobile((String) row[1]);
				student.setProfilePic((String) row[2]);
				student.setApplyForCourse((String) row[3]);
				student.setStudentId((Integer) row[4]);
				student.setCheckInTime((LocalTime) row[5]);
				studentsPresentAndEarlyCheckout.add(student);
			}
		} else {
			List<Object[]> todaysEarlyCheckouts = attendenceRepository.getTodaysEarlyCheckouts(LocalDate.now());
			for (Object[] row : todaysEarlyCheckouts) {
				StudentPresentAndEarlyCheckOut student = new StudentPresentAndEarlyCheckOut();
				student.setFullName((String) row[0]);
				student.setMobile((String) row[1]);
				student.setProfilePic((String) row[2]);
				student.setApplyForCourse((String) row[3]);
				student.setStudentId((Integer) row[4]);
				student.setCheckInTime((LocalTime) row[5]);
				studentsPresentAndEarlyCheckout.add(student);
			}
		}
		return new ResponseEntity<>(studentsPresentAndEarlyCheckout, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getStudentsAttendanceDataForTv(String date) {
		Map<String, Object> response = new HashMap<>();
		LocalDate currentDate = null;
		if (Objects.isNull(date))
			currentDate = LocalDate.now();
		else
			currentDate = LocalDate.parse(date);
		System.out.println(currentDate);
		List<Object[]> dataForTv = studRepo.getStudentAttendanceDataForTv(currentDate);
		List<StudentTvResponse> tvResponse = new ArrayList<>();
		for (Object[] row : dataForTv) {
			StudentTvResponse studentTvResponse = new StudentTvResponse();
			studentTvResponse.setUserId((String) row[0]);
			studentTvResponse.setFullName((String) row[1]);
			studentTvResponse.setProfilePic((String) row[2]);
			studentTvResponse.setCheckInDate((LocalDate) row[3]);
			studentTvResponse.setCheckOutDate((LocalDate) row[4]);
			studentTvResponse.setCheckInTime((LocalTime) row[5]);
			studentTvResponse.setCheckOutTime((LocalTime) row[6]);
			studentTvResponse.setCheckInImage((String) row[7]);
			studentTvResponse.setCheckOutImage((String) row[8]);
			studentTvResponse.setSeatNumber((Integer) row[9]);
			tvResponse.add(studentTvResponse);
		}
		response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
		response.put("studentData", tvResponse);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> getMonthwiseAdmissionCountForYear(Integer year) {
		List<Object[]> monthwiseAdmissionCount = studRepo.getMonthwiseAdmissionCountForYear(year);
		Map<Integer, Long> response = new HashMap<>();
		for (Object[] object : monthwiseAdmissionCount) {
			int monthNumber = ((Number) object[0]).intValue();
			Long admissionCount = ((Long) object[1]);
			response.put(monthNumber, admissionCount);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public ResponseEntity<?> getStudentPresentsAbsentsAndLeavesYearWise(Integer year, Integer studentId) {
		Map<String, Object> response = new HashMap<>();

		// Map<Integer, Long> leavesCount = new HashMap<Integer, Long>();
		Map<Integer, Integer> absentCount = new HashMap<Integer, Integer>();
		Map<Integer, Integer> present = new HashMap<Integer, Integer>();
		Map<Integer, Integer> earlyCheckOut = new HashMap<Integer, Integer>();
		Map<Integer, Integer> mispunch = new HashMap<Integer, Integer>();
		Map<Integer, Integer> leaves = new HashMap<Integer, Integer>();
		// Map<Integer, Long> present = new HashMap<Integer, Long>();

//		List<Object[]> presentForYear = attendenceRepository.getMonthWisePresentForYear(year, studentId);
//		List<Object[]> leaveForYear = leaveRepository.getMonthWiseLeavesForYear(year, studentId);
//
//		for (Object[] object : presentForYear)
//			present.put((Integer) object[0], (Long) object[1]);
//
//		for (Object[] object : leaveForYear)
//			leavesCount.put((Integer) object[0], (Long) object[1]);
//
		int j = studRepo.findById(studentId).get().getJoinDate().getMonthValue();

		if (studRepo.findById(studentId).get().getJoinDate().getYear() == LocalDate.now().getYear()) {
			for (int i = j; i <= LocalDate.now().getMonthValue(); i++) {
				Map<String, Object> calenderData = this.getCalenderData(studentId, i, year);
				StudentCalenderResponse response1 = (StudentCalenderResponse) calenderData.get("StudentCalenderData");
				absentCount.put(i, response1.getAbsent().size());
				present.put(i, response1.getPresent().size());
				earlyCheckOut.put(i, response1.getEarlyCheckOut().size());
				mispunch.put(i, response1.getMispunch().size());
				leaves.put(i, response1.getLeaves().size());

			}
		} else {
			for (int i = 1; i <= 12; i++) {
				Map<String, Object> calenderData = this.getCalenderData(studentId, i, year);
				StudentCalenderResponse response1 = (StudentCalenderResponse) calenderData.get("StudentCalenderData");
				absentCount.put(i, response1.getAbsent().size());
				present.put(i, response1.getPresent().size());
				earlyCheckOut.put(i, response1.getEarlyCheckOut().size());
				mispunch.put(i, response1.getMispunch().size());
				leaves.put(i, response1.getLeaves().size());

			}
		}
		response.put("absents", absentCount);
		response.put("presents", present);
		response.put("leaves", leaves);
		response.put("earlyCheckOuts", earlyCheckOut);
		response.put("mispunchs", mispunch);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> allStudent() {
		List<StudentReponseForWeb> collect = studRepo.getIsCompleted().parallelStream().map(obj -> studentFilter(obj))
				.toList();
		return new ResponseEntity<>(collect, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> deleteTodayAttendance(Integer id) {
		attendenceRepository.deleteAttendanceToday(id, LocalDate.now());
		return new ResponseEntity<>(AppConstants.DELETE_SUCCESS, HttpStatus.OK);
	}

	public AttendenceOfMonth currentMonthAttendenceForDashBoard(Integer studentId, String status) {

		Student student = studRepo.findByStudentId(studentId);

		AttendenceOfMonth obj = new AttendenceOfMonth();
		Long totalLeaves = 0l;
		Long earlyCheckouts = 0l;
		Long mispunch = 0l;
		Long presents = 0l;
		if (status.equals("CURRENT_MONTH")) {
			presents = attendenceRepository.countPresentStudentsForCurrentMonth(studentId);
			mispunch = attendenceRepository.countTotalMishpunchForCurrentMonth(studentId);
			earlyCheckouts = attendenceRepository.countTotalEarlyCheckOutForCurrentMonth(studentId);
			totalLeaves = leaveRepository.countTotalLeavesForCurrentMonth(studentId);
		} else if (status.equals("CURRENT_YEAR")) {
			presents = attendenceRepository.countTotalPresentStudentsForCurrentYear(studentId, student.getJoinDate());
			mispunch = attendenceRepository.countTotalMishpunchForCurrentYear(studentId, student.getJoinDate());
			earlyCheckouts = attendenceRepository.countTotalEarlyCheckOutForCurrentYear(studentId,
					student.getJoinDate());
			totalLeaves = leaveRepository.countTotalLeavesForCurrentYear(studentId, student.getJoinDate());
		}

		Long totalAbsents = 0l;

		if (Objects.isNull(presents))
			presents = 0L;

		if (Objects.isNull(earlyCheckouts))
			earlyCheckouts = 0L;

		if (Objects.isNull(totalLeaves))
			totalLeaves = 0L;

		if (status.equals("CURRENT_MONTH"))
			totalAbsents = (Long) (LocalDate.now().getDayOfMonth()
					- (countSundaysInMonth(LocalDate.now()) + (totalLeaves + earlyCheckouts + presents + mispunch)));
		else if (status.equals("CURRENT_YEAR"))
			totalAbsents = (Long) (ChronoUnit.DAYS.between(student.getJoinDate(), LocalDate.now())
					- (countSundaysUntilCurrentDate(student.getJoinDate())
							+ (totalLeaves + earlyCheckouts + presents + mispunch)));

		obj.setTotalPresent(presents);
		obj.setTotalMispunch(mispunch);
		obj.setTotalEarlyCheckOut(earlyCheckouts);
		obj.setTotalAbsent(totalAbsents);
		obj.setTotalLeaves(totalLeaves);
		return obj;
	}

	public long countSundaysInMonth(LocalDate currentDate) {
		LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
		long daysBetween = ChronoUnit.DAYS.between(firstDayOfMonth, currentDate);
		long sundays = 0;

		for (long i = 0; i <= daysBetween; i++) {
			LocalDate date = firstDayOfMonth.plusDays(i);
			if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
				sundays++;
			}
		}
		return sundays;
	}

	public long countSundaysUntilCurrentDate(LocalDate joiningDate) {
		LocalDate currentDate = LocalDate.now();
		long sundays = 0;

		while (!joiningDate.isAfter(currentDate)) {
			if (joiningDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
				sundays++;
			}
			joiningDate = joiningDate.plusDays(1);
		}
		return sundays;
	}

	public ResponseEntity<?> getTodaysPresentAbsentEarlyCheckOutsMispunchAndLeaves() {
		Map<String, Object> response = new HashMap<>();
		response.put("earlyCheckOut", attendenceRepository.getTodayEarlyCheckOutsCount());
		response.put("present", studRepo.getTotalPresentToday(LocalDate.now()));
		response.put("absent", attendenceRepository.getTodayAbsentCount());
		response.put("leaves", studRepo.getTotalOnLeavesCount());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public void fetchRandomStudentForMockInterview() {

		List<MockInterview> mock = new ArrayList<>();

		List<MockInterview> mockDb = mockRepo.findAll();

		List<Student> list = studRepo.getIsCompleted();

		for (int i = 1; i <= 1; i++) {
			Random random = new Random();
			int randomIndex = random.nextInt(list.size());
			Student student = list.get(randomIndex);

			boolean isMatch1 = mock.parallelStream()
					.anyMatch(obj -> obj.getStudent().getStudentId() == student.getStudentId());
			boolean isMatch2 = mockDb.parallelStream()
					.anyMatch(obj -> obj.getStudent().getStudentId() == student.getStudentId());

			if (!isMatch1 && !isMatch2) {
				MockInterview newMock = new MockInterview();
				newMock.setIsCompleted(false);
				newMock.setMockDate(LocalDate.now());
				newMock.setStudent(student);
				mock.add(mockRepo.save(newMock));
			} else {
				--i;
			}
		}
		mockRepo.saveAll(mock);
	}

	@Override
	public void fetchRandomStudentForCounselling() {
		List<CounsellingInterview> counselling = new ArrayList<>();

		List<CounsellingInterview> CounsellingDb = counsellingRepo.findAll();

		List<Student> list = studRepo.getIsCompleted();

		for (int i = 1; i <= 1; i++) {
			Random random = new Random();
			int randomIndex = random.nextInt(list.size());
			Student student = list.get(randomIndex);

			boolean isMatch1 = counselling.parallelStream()
					.anyMatch(obj -> obj.getStudent().getStudentId() == student.getStudentId());
			boolean isMatch2 = CounsellingDb.parallelStream()
					.anyMatch(obj -> obj.getStudent().getStudentId() == student.getStudentId());

			if (!isMatch1 && !isMatch2) {
				CounsellingInterview newCounselling = new CounsellingInterview();
				newCounselling.setIsCompleted(false);
				newCounselling.setCounsellingDate(LocalDate.now());
				newCounselling.setStudent(student);
				counselling.add(counsellingRepo.save(newCounselling));
			} else {
				--i;
			}
		}
		counsellingRepo.saveAll(counselling);
	}

	@Override
	public void checkMockIsCompleteOrNot() {

		List<MockInterview> list = mockRepo.findbycurrentDay();
		if (!list.isEmpty()) {
			list.forEach(obj -> {
				Attendance attendance = attendenceRepository
						.findByStudentIdAndCheckInDate(obj.getStudent().getStudentId(), LocalDate.now());
				if (Objects.nonNull(attendance)) {
					obj.setIsCompleted(true);
					mockRepo.save(obj);
				} else {
					mockRepo.delete(obj);
				}
			});
		}

		if (mockRepo.findAll().size() == studRepo.getIsCompleted().size())
			mockRepo.deleteAll();
	}

	@Override
	public void checkCounsellingkIsCompleteOrNot() {

		List<CounsellingInterview> list = counsellingRepo.findbycurrentDay();

		list.forEach(obj -> {
			Attendance attendance = attendenceRepository.findByStudentIdAndCheckInDate(obj.getStudent().getStudentId(),
					LocalDate.now());
			if (Objects.nonNull(attendance)) {
				obj.setIsCompleted(true);
				counsellingRepo.save(obj);
			} else {
				counsellingRepo.delete(obj);
			}
		});

		if (counsellingRepo.findAll().size() == studRepo.getIsCompleted().size())
			counsellingRepo.deleteAll();
	}

	@Override
	public MockResponse checkMockForStudent(Integer studentId) {

		MockInterview obj = mockRepo.findByStudentIdAndCurrentDate(studentId);

		MockResponse response = new MockResponse();

		if (Objects.nonNull(obj)) {
			response.setMockPerson("Kamal Gupta");
			response.setMockDate(obj.getMockDate());
			response.setIsMock(true);
			return response;
		}

		response.setIsMock(false);
		return response;
	}

	@Override
	public CounsellingResponse checkCounsellingForStudent(Integer studentId) {

		CounsellingInterview obj = counsellingRepo.findByStudentIdAndCurrentDate(studentId);

		CounsellingResponse response = new CounsellingResponse();
		if (Objects.nonNull(obj)) {
			response.setCounsellingPerson("Kamal Gupta");
			response.setCounsellingDate(obj.getCounsellingDate());
			response.setIsCounselling(true);
			return response;
		}

		response.setIsCounselling(false);
		return response;

	}

	@Override
	public ResponseEntity<?> studentDeviceChangeApi(String userId) {
		Student findByUserId = studRepo.findByUserId(userId);
		if (Objects.nonNull(findByUserId)) {
			findByUserId.setDeviceId("");
			findByUserId.setInUseDeviceId("");
			studRepo.save(findByUserId);
			return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("STUDENT_NOT_FOUND", HttpStatus.OK);
		}

	}

	public StudentReponseForWeb studentFilter(Student student) {

		CourseResponse course = new CourseResponse();
		StudentReponseForWeb response = new StudentReponseForWeb();
		response.setApplyForCourse(student.getApplyForCourse());
		response.setStudentId(student.getStudentId());
		response.setUserId(student.getUserId());
		response.setFullName(student.getFullName());
		response.setMobile(student.getMobile());
		response.setDob(student.getDob());
		response.setJoinDate(student.getJoinDate());
		// response.setInUseDeviceId(student.getInUseDeviceId());
		response.setProfilePic(student.getProfilePic());
		response.setCurrentCourse(student.getCurrentCourse());
		response.setEmail(student.getEmail());
		if (Objects.nonNull(student.getCourse())) {
			course.setCourseFees(student.getCourse().getCourseFees());
			course.setCourseId(student.getCourse().getCourseId());
			course.setCourseName(student.getCourse().getCourseName());
		}
		response.setCourseResponse(course);
		return response;
	}

	@Override
	public ResponseEntity<?> allFeesRemainingStudent() {
		List<StudentReponseForWeb> students = studRepo.allFeesRemainingStudent();
		return new ResponseEntity<>(students, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> updateFcmId(HttpHeaders header, String fcmId) {

		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Integer studentId = Integer.parseInt(
				util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());
		Student student = studRepo.findByUserIdAndIsActive(username, true).get();
		Boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());

		Map<String, Object> map = new HashMap<>();

		if (validateToken) {
			studRepo.updateFcmId(fcmId, studentId);
			map.put(AppConstants.MESSAGE, AppConstants.UPDATE_SUCCESSFULLY);
			return new ResponseEntity<>(map, HttpStatus.OK);
		} else
			map.put(AppConstants.MESSAGE, AppConstants.UNAUTHORIZED);

		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	// getting task statics data of student

	@Override
	public ResponseEntity<?> getTaskStatics(Integer studentId) {
		// TODO Auto-generated method stub

		Map<String, Object> response = new HashMap<>();

		List<Object[]> data = studRepo.getTaskStatics(studentId, LocalDate.now().getMonthValue());
		int currentMonthLength = LocalDate.now().lengthOfMonth();

		Long[] totalSubmitted = new Long[currentMonthLength];
		Long[] totalAccepted = new Long[currentMonthLength];
		Long[] totalRejected = new Long[currentMonthLength];
		Long[] categories = new Long[currentMonthLength];

		Arrays.fill(totalSubmitted, 0L); // Initialize the days array with zeros
		Arrays.fill(totalRejected, 0L);
		Arrays.fill(totalAccepted, 0L);
		
		for (int i = 0; i < currentMonthLength; i++) {
		    categories[i] = (long) (i + 1); // Assigning values from 1 to currentMonthLength
		}
		data.forEach(d -> {
			// Assuming the data format is [dayOfMonth, taskCount]
			Integer dayOfMonth = (Integer) d[0];
			Long taskCount = (Long) d[1];

			if (dayOfMonth > 0 && dayOfMonth <= currentMonthLength) {
				totalSubmitted[(int) (dayOfMonth - 1)] = taskCount; // Store taskCount in the corresponding day
			}
			totalAccepted[(int) (dayOfMonth - 1)] = (Long) d[2];
			totalRejected[(int) (dayOfMonth - 1)] = (Long) d[3];

		});
		

		response.put("totalSubmitted", totalSubmitted);
		response.put("totalAccepted", totalAccepted);
		response.put("totalRejected", totalRejected);
		response.put("categories", categories);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
