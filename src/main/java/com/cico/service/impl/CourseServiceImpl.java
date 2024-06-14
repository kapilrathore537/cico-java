package com.cico.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cico.exception.ResourceAlreadyExistException;
import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Course;
import com.cico.model.Student;
import com.cico.model.Subject;
import com.cico.model.TechnologyStack;
import com.cico.payload.ApiResponse;
import com.cico.payload.BatchResponse;
import com.cico.payload.CourseRequest;
import com.cico.payload.CourseResponse;
import com.cico.payload.PageResponse;
import com.cico.payload.SubjectResponse;
import com.cico.payload.TechnologyStackResponse;
import com.cico.repository.CourseRepository;
import com.cico.repository.StudentRepository;
import com.cico.repository.SubjectRepository;
import com.cico.repository.TechnologyStackRepository;
import com.cico.service.ICourseService;
import com.cico.util.AppConstants;

@Service
public class CourseServiceImpl implements ICourseService {

	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private TechnologyStackRepository repository;
	@Autowired
	private SubjectRepository subjectRepository;
	@Autowired
	private StudentRepository studentRepository;

	public static final String COURSE_ADD_SUCCESS = "Course Add Successfully";
	public static final String COURSE_NOT_FOUND = "Course Not Found";
	public static final String COURSE_UPDATE_SUCCESS = "Course Update Successfully";
	public static final String COURSE_UPGRADE_SUCCESS = "Course Update Successfully";

	@Override
	public ResponseEntity<?> createCourse(CourseRequest request) {

		Course isPresent = courseRepository.findByCourseNameAndIsDeletedFalse(request.getCourseName().trim());
		if (isPresent != null) {
			throw new ResourceAlreadyExistException("Course already exist with this name.");
		}

		Map<String, Object> response = new HashMap<>();
		Course course = new Course(request.getCourseName(), request.getCourseFees(), request.getDuration(),
				request.getSortDescription(), null, request.getIsStarterCourse());
		List<Subject> subjects = course.getSubjects();
		for (Integer id : request.getSubjectIds()) {
			subjects.add(subjectRepository.findBySubjectIdAndIsDeleted(id).get());
		}
		course.setSubjects(subjects);
		course.setCreatedDate(LocalDate.now());
		course.setUpdatedDate(LocalDate.now());
		course.setTechnologyStack(repository.findById(request.getTechnologyStack()).get());
		Course savedCourse = courseRepository.save(course);
		if (Objects.nonNull(savedCourse)) {
			response.put(AppConstants.MESSAGE, COURSE_ADD_SUCCESS);
			response.put("course", savedCourse);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}
		response.put(AppConstants.MESSAGE, AppConstants.FAILED);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public CourseResponse findCourseById(Integer courseId) {

		Course course = courseRepository.findByCourseIdAndIsDeleted(courseId, false);
		CourseResponse res = new CourseResponse();
		res.setCourseId(course.getCourseId());
		res.setCourseName(course.getCourseName());
		res.setCourseFees(course.getCourseFees());
		res.setSortDescription(course.getSortDescription());
		res.setDuration(course.getDuration());
		res.setIsStarterCourse(course.getIsStarterCourse());

		// subject filter
		List<SubjectResponse> subjectResponses = new ArrayList<>();
		course.getSubjects().stream().map(obj -> {

			if (!obj.getIsDeleted()) {
				SubjectResponse res1 = new SubjectResponse();
				res1.setSubjectId(obj.getSubjectId());
				res1.setSubjectName(obj.getSubjectName());
				TechnologyStackResponse stackResponse = new TechnologyStackResponse();
				stackResponse.setId(obj.getTechnologyStack().getId());
				stackResponse.setImageName(obj.getTechnologyStack().getImageName());
				stackResponse.setTechnologyName(obj.getTechnologyStack().getTechnologyName());
				res1.setTechnologyStack(stackResponse);
				subjectResponses.add(res1);
				return res1;
			}
			return null;

		}).collect(Collectors.toList());

		List<BatchResponse> batchResponses = new ArrayList<>();

		course.getBatches().forEach(obj -> {

			if (!obj.isDeleted()) {
				BatchResponse batchResponse = new BatchResponse();

				batchResponse.setBatchId(obj.getBatchId());
				batchResponse.setBatchName(obj.getBatchName());
				batchResponse.setBatchStartDate(obj.getBatchStartDate());
				batchResponse.setBatchTiming(obj.getBatchTiming());
				batchResponse.setBatchDetails(obj.getBatchDetails());

				SubjectResponse response = new SubjectResponse();

				response.setSubjectId(obj.getSubject().getSubjectId());
				response.setSubjectName(obj.getSubject().getSubjectName());

				TechnologyStackResponse stackResponse = new TechnologyStackResponse();
				stackResponse.setId(obj.getSubject().getTechnologyStack().getId());
				stackResponse.setImageName(obj.getSubject().getTechnologyStack().getImageName());
				stackResponse.setTechnologyName(obj.getSubject().getTechnologyStack().getTechnologyName());

				response.setTechnologyStack(stackResponse);
				batchResponse.setSubject(response);

				batchResponses.add(batchResponse);
			}

		});

		TechnologyStackResponse stackResponse = new TechnologyStackResponse();
		stackResponse.setId(course.getTechnologyStack().getId());
		stackResponse.setImageName(course.getTechnologyStack().getImageName());
		stackResponse.setTechnologyName(course.getTechnologyStack().getTechnologyName());

		res.setBatchesCount((long) batchResponses.size());
		res.setSubjectCount((long) subjectResponses.size());
		res.setBatchResponse(batchResponses);
		res.setSubjectResponse(subjectResponses);

		TechnologyStackResponse stackResponse2 = new TechnologyStackResponse();
		stackResponse2.setId(course.getTechnologyStack().getId());
		stackResponse2.setImageName(course.getTechnologyStack().getImageName());
		;
		stackResponse2.setTechnologyName(course.getTechnologyStack().getTechnologyName());
		res.setTechnologyStack(stackResponse2);
		return res;
	}


	@Override
	public ResponseEntity<?> getAllCourses(Integer page, Integer size) {
		if (page != -1) {
			PageRequest p = PageRequest.of(page, size, Sort.by(Direction.DESC, "courseId"));
			Page<Object[]> findAllByIsDeleted = courseRepository.findAllByIsDeleted(false, p);

			List<CourseResponse> courseResponses = new ArrayList<>();

			for (Object[] row : findAllByIsDeleted.getContent()) {
				CourseResponse res = new CourseResponse();
				res.setBatchesCount((Long) row[4]);
				res.setCourseId((Integer) row[0]);
				res.setSubjectCount((Long) row[3]);
				res.setCourseName((String) row[1]);

				TechnologyStackResponse stackResponse = new TechnologyStackResponse();
				stackResponse.setId((Integer) row[5]);
				stackResponse.setImageName((String) row[2]);

				res.setTechnologyStack(stackResponse);
				courseResponses.add(res);
			}

			PageResponse<CourseResponse> pageResponse = new PageResponse<>(courseResponses,
					findAllByIsDeleted.getNumber(), findAllByIsDeleted.getSize(),
					findAllByIsDeleted.getNumberOfElements(), findAllByIsDeleted.getTotalPages(),
					findAllByIsDeleted.isLast());
			return new ResponseEntity<>(pageResponse, HttpStatus.OK);
		} else {
			List<CourseResponse> courseResponses = new ArrayList<>();
			List<Object[]> findByIsDeleted = courseRepository.findByIsDeleted(false);
			for (Object[] row : findByIsDeleted) {
				CourseResponse res = new CourseResponse();
				res.setBatchesCount((Long) row[3]);
				res.setCourseId((Integer) row[0]);
				res.setSubjectCount((Long) row[2]);
				res.setCourseName((String) row[1]);
				courseResponses.add(res);
			}

			return new ResponseEntity<>(courseResponses, HttpStatus.OK);
		}
	}

	@Override
	public ApiResponse updateCourse(CourseRequest course) {

		Course course1 = courseRepository.findById(course.getCourseId()).get();
		if (course1 == null) {
			throw new ResourceNotFoundException("Course not found.");
		}

		Course isPresent = courseRepository.findByCourseNameAndIsDeletedFalse(course.getCourseName());

		if (isPresent != null && isPresent.getCourseId() != course1.getCourseId()) {
			throw new ResourceAlreadyExistException("Course already exist with this name.");
		}

		course1.setCourseName(course.getCourseName());
		course1.setCourseFees(course.getCourseFees());
		course1.setDuration(course.getDuration());
		course1.setSortDescription(course.getSortDescription());
		course1.setIsStarterCourse(course.getIsStarterCourse());

		Optional<TechnologyStack> findById = repository.findById(course.getTechnologyStack());
		course1.setTechnologyStack(findById.get());
		course1.setSubjects(subjectRepository.findAllById(course.getSubjectIds()));
		Course save = courseRepository.save(course1);

		if (Objects.nonNull(save)) {
			return new ApiResponse(Boolean.TRUE, COURSE_UPDATE_SUCCESS, HttpStatus.CREATED);
		}
		return new ApiResponse(Boolean.FALSE, AppConstants.FAILED, HttpStatus.OK);

	}

	@Override
	public Boolean deleteCourseById(Integer courseId) {
		int deleteCourse = courseRepository.deleteCourse(courseId);
		if (deleteCourse != 0)
			return true;
		return false;
	}

	@Override
	public Map<String, Object> studentUpgradeCourse(Integer studnetId, Integer courseId) {
		Map<String, Object> response = new HashMap<>();
		Student findByStudentId = studentRepository.findByStudentId(studnetId);
		Optional<Course> findByCourseId = courseRepository.findByCourseId(courseId);
		findByCourseId.get().setCourseFees(findByStudentId.getCourse().getCourseFees());
		findByStudentId.setApplyForCourse(findByCourseId.get().getCourseName());
		findByStudentId.setCourse(findByCourseId.get());
		Student save = studentRepository.save(findByStudentId);

		CourseResponse courseResponse = new CourseResponse();
		courseResponse.setCourseId(findByCourseId.get().getCourseId());
		courseResponse.setCourseName(findByCourseId.get().getCourseName());
//		if (Objects.nonNull(save))
//			return new ApiResponse(Boolean.TRUE, COURSE_UPGRADE_SUCCESS, HttpStatus.CREATED);
//		return new ApiResponse(Boolean.FALSE, AppConstants.FAILED, HttpStatus.OK)
		response.put("course", courseResponse);

		return response;
	}

	@Override
	public ResponseEntity<?> getCourseProgress(Integer studentId) {
		Map<String, Object> response = new HashMap<>();
		Student findByStudentId = studentRepository.findByStudentId(studentId);
		String duration = findByStudentId.getCourse().getDuration();
		System.out.println(duration);
		long months = Long.parseLong(duration);

		LocalDate joinDate = findByStudentId.getJoinDate();
		LocalDate endDate = joinDate.plusMonths(months);
		LocalDate currentDate = LocalDate.now();

		long daysElapsed = ChronoUnit.DAYS.between(joinDate, currentDate);
		long totalDays = ChronoUnit.DAYS.between(joinDate, endDate);

		double percentageCompletion = (double) daysElapsed / totalDays * 100;

		response.put("percentage", percentageCompletion > 100 ? 100 : percentageCompletion);
		response.put("courseName", findByStudentId.getCourse().getCourseName());
		response.put("joinDate", joinDate);
		response.put("endDate", endDate);
		response.put("image", findByStudentId.getCourse().getTechnologyStack().getImageName());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getCoureWithBatchesAndSubjects(Integer courseId) {
		List<Object[]> coureWithBatchesAndSubjects = courseRepository.getCoureWithBatchesAndSubjects(courseId);
		Set<Integer> set = new HashSet<>();
		Set<String> set1 = new HashSet<>();

		for (Object[] row : coureWithBatchesAndSubjects) {
			set.add((Integer) row[0]);
			set1.add((String) row[1]);
		}

		System.err.println(set);
		System.err.println(set1);

		return null;
	}

	@Override
	public ResponseEntity<?> getAllNonStarterCourses() {

		List<Object[]> allNonStarterCourses = courseRepository.getAllNonStarterCourses();
		List<CourseResponse> list = new ArrayList<>();
		Map<String, Object> response = new HashMap<>();

		if (Objects.isNull(allNonStarterCourses)) {

			response.put(AppConstants.MESSAGE, AppConstants.NO_DATA_FOUND);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

		} else {
			for (Object[] row : allNonStarterCourses) {
				CourseResponse res = new CourseResponse();
				res.setCourseId((Integer) row[0]);
				res.setCourseName((String) row[1]);
				TechnologyStackResponse stackResponse = new TechnologyStackResponse();
				stackResponse.setId((Integer) row[3]);
				stackResponse.setImageName((String) row[2]);
				res.setTechnologyStack(stackResponse);
				list.add(res);
			}
			response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
			response.put("NonStarterCourse", list);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

	}

	public CourseResponse courseToCourseResponse(Course course) {
		CourseResponse res = new CourseResponse();
		res.setCourseId(course.getCourseId());
		res.setCourseName(course.getCourseName());
		res.setDuration(course.getDuration());
		res.setSortDescription(course.getSortDescription());
		TechnologyStackResponse stackResponse = new TechnologyStackResponse();
		stackResponse.setId(course.getTechnologyStack().getId());
		stackResponse.setImageName(course.getTechnologyStack().getImageName());
		res.setTechnologyStack(stackResponse);
		return res;
	}

	@Override
	public ResponseEntity<?> getAllStarterCourses() {
		List<Object[]> allNonStarterCourses = courseRepository.getAllStarterCourses();
		List<CourseResponse> list = new ArrayList<>();
		Map<String, Object> response = new HashMap<>();

		if (Objects.isNull(allNonStarterCourses)) {

			response.put(AppConstants.MESSAGE, AppConstants.NO_DATA_FOUND);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

		} else {
			for (Object[] row : allNonStarterCourses) {
				CourseResponse res = new CourseResponse();
				res.setCourseId((Integer) row[0]);
				res.setCourseName((String) row[1]);
				TechnologyStackResponse stackResponse = new TechnologyStackResponse();
				stackResponse.setId((Integer) row[3]);
				stackResponse.setImageName((String) row[2]);
				res.setTechnologyStack(stackResponse);
				list.add(res);
			}
			response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
			response.put("NonStarterCourse", list);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<?> getAllCourseForStudent(Integer studentId) {

		Map<String, Object> response = new HashMap<>();

		Optional<Student> student = studentRepository.findById(studentId);
		if (student.isPresent()) {
			List<Course> allCourses = courseRepository.findAll();
			List<CourseResponse> list = allCourses.stream()
					.filter(obj -> obj.getCourseId() != student.get().getCourse().getCourseId() && !obj.getIsDeleted())
					.map(this::courseToCourseResponse).toList();

			response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
			response.put("courses", list);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			response.put(AppConstants.MESSAGE, AppConstants.NO_DATA_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

	}

}
