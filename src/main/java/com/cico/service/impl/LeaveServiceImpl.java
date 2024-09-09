package com.cico.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cico.exception.ResourceAlreadyExistException;
import com.cico.model.LeaveType;
import com.cico.model.Leaves;
import com.cico.model.Student;
import com.cico.payload.LeaveResponse;
import com.cico.payload.PageResponse;
import com.cico.repository.LeaveRepository;
import com.cico.repository.LeaveTypeRepository;
import com.cico.repository.StudentRepository;
import com.cico.security.JwtUtil;
import com.cico.service.ILeaveService;
import com.cico.util.AppConstants;

@Service
public class LeaveServiceImpl implements ILeaveService {

	@Autowired
	private LeaveTypeRepository leaveTypeRepository;

	@Autowired
	private StudentRepository studRepo;

	@Autowired
	private JwtUtil util;

	@Autowired
	private LeaveRepository leavesRepository;
	
	@Autowired
	private StudentServiceImpl studentServiceImpl;

	@Autowired
	private ModelMapper mapper;

	@Override
	public ResponseEntity<?> addStudentLeave(HttpHeaders header, Integer leaveTypeId, String leaveStartDate,
			String leaveEndDate, String leaveReason, String leaveDayType, String halfDayType) {
		Map<String, Object> response = new HashMap<>();
		long dateDiff = 0;
		int i = 0;
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Integer studentId = Integer.parseInt(
				util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());
		Student student = studRepo.findByUserIdAndIsActive(username, true).get();
		Boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());
		
		Optional<Leaves> checkLeavesData = leavesRepository.findByStudentIdAndLeaveStartDateAndEndDate(studentId,LocalDate.parse(leaveStartDate));
		if(checkLeavesData.isPresent())
			throw new ResourceAlreadyExistException("Already Leave Apply For Given Date");
		
		LocalDate toDate = null;
		LocalDate fromDate = null;

		if (leaveStartDate != null && !leaveStartDate.equals("")) {
			toDate = LocalDate.parse(leaveStartDate);
		} else {
			toDate = LocalDate.now();
		}

		if (leaveEndDate != null && !leaveEndDate.equals("")) {
			fromDate = LocalDate.parse(leaveEndDate);
		} else {
			fromDate = toDate;
		}

		if (validateToken) {
			if (leaveTypeId != null && leaveStartDate != null && leaveReason != null
					&& (leaveDayType.equals("Full Day") || leaveDayType.equals("Half Day"))) {
				Leaves leavesData = new Leaves(studentId, leaveTypeId, leaveReason, leaveDayType);
				if (leaveDayType.equals("Half Day")) {
					if (halfDayType.equals("First Half") || halfDayType.equals("Second Half")) {
						leavesData.setHalfDayType(halfDayType);
						leavesData.setCreatedDate(LocalDateTime.now());
						leavesData.setLeaveDate(toDate);
						leavesData.setLeaveEndDate(toDate);
						leavesData.setLeaveDuration(0);

						leavesRepository.save(leavesData);
						response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
						return new ResponseEntity<>(response, HttpStatus.OK);
					} else {
						response.put(AppConstants.MESSAGE, AppConstants.ALL_REQUIRED);
						return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
					}
				}
				if (leaveDayType.equals("Full Day")) {
					if (Objects.nonNull(leaveEndDate)) {
						System.out.println(leaveEndDate);
						System.out.println(leaveStartDate);
						leavesData.setLeaveDate(toDate);
						leavesData.setLeaveEndDate(fromDate);
						dateDiff = (int) ChronoUnit.DAYS.between(toDate, fromDate);
						leavesData.setCreatedDate(LocalDateTime.now());
						leavesData.setLeaveDuration((int) dateDiff + 1);
						System.out.println(dateDiff);
					} else {
						response.put(AppConstants.MESSAGE, AppConstants.ALL_REQUIRED);
						return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
					}
				}
				
				if (!leaveDayType.isEmpty()) {
					if (dateDiff >= 0) {
						Leaves leaves = null;
						for (i = 0; i <= dateDiff; i++) {
							if (i == 0) {
								leavesData.setLeaveDate(toDate);

								leavesData.setLeaveEndDate(fromDate);
							} else {

								leavesData.setLeaveDate(toDate);
								leavesData.setCreatedDate(LocalDateTime.now());
							}
							
							 leaves = leavesRepository.save(leavesData);
						}
						if (Objects.nonNull(leaves)) {
							response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
							return new ResponseEntity<>(response, HttpStatus.OK);
						} else {
							response.put(AppConstants.MESSAGE, AppConstants.FAILED);
							return new ResponseEntity<>(response, HttpStatus.OK);
						}
					} else {
						response.put(AppConstants.MESSAGE, AppConstants.INVALID_DATE);
						return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
					}
				} else {
					response.put(AppConstants.MESSAGE, AppConstants.ALL_REQUIRED);
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
	public ResponseEntity<?> getStudentLeaves(Integer studentId, Integer page, Integer size) {
		Map<String, Object> response = new HashMap<>();
		List<LeaveResponse> leavesResponse = new ArrayList<>();

			Page<Leaves> StudentLeaves = leavesRepository.findStudentLeaves(studentId,
					PageRequest.of(page, size, Sort.by(Direction.DESC, "leaveId")));

			for (Leaves leaves : StudentLeaves.getContent()) {
				if ( leaves.getLeaveStatus() == 0  && ChronoUnit.DAYS.between(leaves.getLeaveEndDate(), LocalDateTime.now()) >=1) {
					studentServiceImpl.approveStudentLeaveReqeust(studentId, leaves.getLeaveId(), "deny");
					leaves.setLeaveStatus(2);
				//leaves.setLeaveStatusChangeDate(LocalDateTime.now());
				}

				LeaveResponse responseData = mapper.map(leaves, LeaveResponse.class);
				responseData.setLeaveType(leaveTypeRepository.findById(leaves.getLeaveTypeId()).get());
				leavesResponse.add(responseData);

			}

			int totalLeaves = leavesRepository.countByStudentId(studentId);
			if (Objects.nonNull(StudentLeaves)) {
				response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
				response.put("leavesData",
						new PageResponse<>(leavesResponse, StudentLeaves.getNumber(), StudentLeaves.getSize(),
								StudentLeaves.getTotalElements(), StudentLeaves.getTotalPages(),
								StudentLeaves.isLast()));
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put(AppConstants.MESSAGE, AppConstants.FAILED);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
	}
	
	@Override
	public ResponseEntity<?> getStudentLeavesByToken(HttpHeaders header, Integer page, Integer size) {
		Map<String, Object> response = new HashMap<>();
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Integer studentId = Integer.parseInt(
				util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());

		boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), username);
		List<LeaveResponse> leavesResponse = new ArrayList<>();

		if (validateToken) {
			Page<Leaves> StudentLeaves = leavesRepository.findStudentLeaves(studentId,
					PageRequest.of(page, size, Sort.by(Direction.DESC, "leaveId")));

			for (Leaves leaves : StudentLeaves.getContent()) {
				LeaveResponse responseData = mapper.map(leaves, LeaveResponse.class);
				responseData.setLeaveType(leaveTypeRepository.findById(leaves.getLeaveTypeId()).get());
				leavesResponse.add(responseData);
			}

			int totalLeaves = leavesRepository.countByStudentId(studentId);
			if (Objects.nonNull(StudentLeaves)) {
				response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
				response.put("leavesData",
						new PageResponse<>(leavesResponse, StudentLeaves.getNumber(), StudentLeaves.getSize(),
								StudentLeaves.getTotalElements(), StudentLeaves.getTotalPages(),
								StudentLeaves.isLast()));
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
	public Map<String, Object> deleteStudentLeave(HttpHeaders header, Integer leaveId) {
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Integer studentId = Integer.parseInt(
				util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());

		boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), username);
		Map<String, Object> map = new HashMap<>();

		if (validateToken) {
			Leaves findByStudentIdAndLeaveId = leavesRepository.findByStudentIdAndLeaveId(studentId, leaveId);
			if (findByStudentIdAndLeaveId != null) {
				if (findByStudentIdAndLeaveId.getLeaveStatus() == 0) {
					int deleteByStudnetIdLeaveId = leavesRepository.deleteByStudnetIdLeaveId(studentId, leaveId);
					if (Objects.nonNull(deleteByStudnetIdLeaveId)) {
						map.put(AppConstants.MESSAGE, AppConstants.SUCCESS);

					} else {
						map.put(AppConstants.MESSAGE, AppConstants.FAILED);
					}
				} else {
					map.put(AppConstants.MESSAGE, "LEAVE_APPROVE_REJECT");
				}
			} else {
				map.put(AppConstants.MESSAGE, AppConstants.INVALID_ID);
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> retractStudentLeave(HttpHeaders header, Integer leaveId) {
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Integer studentId = Integer.parseInt(
				util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());

		boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), username);
		Map<String, Object> map = new HashMap<>();

		if (validateToken) {
			Leaves findByStudentIdAndLeaveId = leavesRepository.findByStudentIdAndLeaveId(studentId, leaveId);
			if (findByStudentIdAndLeaveId != null) {
				if (findByStudentIdAndLeaveId.getLeaveStatus() == 1) {
					if (findByStudentIdAndLeaveId.getRetractLeave() == 0) {
						int deleteByStudnetIdLeaveId = leavesRepository.deleteByStudnetIdLeaveIdStudentId(studentId,
								leaveId);
						if (Objects.nonNull(deleteByStudnetIdLeaveId)) {
							map.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
						} else {
							map.put(AppConstants.MESSAGE, AppConstants.FAILED);
						}
					} else {
						map.put(AppConstants.MESSAGE, "ALREADY_RETRACT_LEAVE");
					}
				} else {
					map.put(AppConstants.MESSAGE, "LEAVE_PENDING");
				}
			} else {
				map.put(AppConstants.MESSAGE, AppConstants.INVALID_ID);
			}
		}
		return map;
	}

	@Override
	public ResponseEntity<?> studentLeaveMonthFilterById(Integer studentId, Integer monthNo) {
		Map<String, Object> response = new HashMap<>();
		List<LeaveResponse> leavesResponse = new ArrayList<>();
			  Page<Leaves> StudentLeaves = leavesRepository.findByStudentIdAndMonthNo(studentId, monthNo,PageRequest.of(0, 30, Sort.by(Direction.DESC, "leaveId")));
			for (Leaves leaves : StudentLeaves.getContent()) {
				LeaveResponse responseData = mapper.map(leaves, LeaveResponse.class);
				responseData.setLeaveType(leaveTypeRepository.findById(leaves.getLeaveTypeId()).get());
				leavesResponse.add(responseData);
			}

			int totalLeaves = leavesRepository.countByStudentId(studentId);
			if (Objects.nonNull(StudentLeaves)) {
				response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
				response.put("leavesData",
						new PageResponse<>(leavesResponse, StudentLeaves.getNumber(), StudentLeaves.getSize(),
								StudentLeaves.getTotalElements(), StudentLeaves.getTotalPages(),
								StudentLeaves.isLast()));
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put(AppConstants.MESSAGE, AppConstants.FAILED);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
	}
	
	
	@Override
	public ResponseEntity<?> studentLeaveMonthFilterByToken(HttpHeaders header, Integer monthNo) {
		Map<String, Object> response = new HashMap<>();
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Integer studentId = Integer.parseInt(
				util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());

		boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), username);
		List<LeaveResponse> leavesResponse = new ArrayList<>();

		if (validateToken) {
			  Page<Leaves> StudentLeaves = leavesRepository.findByStudentIdAndMonthNo(studentId, monthNo,PageRequest.of(0, 30, Sort.by(Direction.DESC, "leaveId")));
			for (Leaves leaves : StudentLeaves.getContent()) {
				LeaveResponse responseData = mapper.map(leaves, LeaveResponse.class);
				responseData.setLeaveType(leaveTypeRepository.findById(leaves.getLeaveTypeId()).get());
				leavesResponse.add(responseData);
			}

			int totalLeaves = leavesRepository.countByStudentId(studentId);
			if (Objects.nonNull(StudentLeaves)) {
				response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
				response.put("leavesData",
						new PageResponse<>(leavesResponse, StudentLeaves.getNumber(), StudentLeaves.getSize(),
								StudentLeaves.getTotalElements(), StudentLeaves.getTotalPages(),
								StudentLeaves.isLast()));
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
	public ResponseEntity<?> getAllLeavesType() {
		Map<String, Object> response = new HashMap<>();
		List<LeaveType> leaveTypeList = leaveTypeRepository.findByIsActiveAndIsDelete(true, false);
		if (leaveTypeList.isEmpty()) {
			response.put(AppConstants.MESSAGE, AppConstants.FAILED);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
		response.put("leaveType", leaveTypeList);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
