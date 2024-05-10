package com.cico.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cico.payload.ApiResponse;
import com.cico.payload.CourseRequest;
import com.cico.payload.CourseResponse;
import com.cico.service.ICourseService;
import com.cico.util.AppConstants;

@RestController
@RequestMapping("/course")
@CrossOrigin("*")
public class CourseController {

	@Autowired
	private ICourseService courseService;

	@PostMapping("/addCourseApi")
	public ResponseEntity<?> createCourse(@RequestBody CourseRequest courseRequest) {
		 return courseService.createCourse(courseRequest);
	}

	
	@GetMapping("/findCourseByIdApi")
	public ResponseEntity<CourseResponse> getCourseById(@RequestParam("courseId") Integer courseId) {
		 CourseResponse courseResponse = courseService.findCourseById(courseId);
		return ResponseEntity.ok(courseResponse);
	}

	@GetMapping("/findAllCourseApi")
	public ResponseEntity<?> getAllCourses(
			@RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		return courseService.getAllCourses(page, size);
		
	}
	
	@PutMapping("/updateCourseApi")
	public ResponseEntity<?> updateCourse(@RequestBody CourseRequest course) {
		ApiResponse updateCourse = courseService.updateCourse(course);
		return ResponseEntity.status(HttpStatus.CREATED).body(updateCourse);
	}

	@PutMapping("/deleteCourseByIdApi")
	public ResponseEntity<ApiResponse> deleteCourseById(@RequestParam("courseId") Integer courseId) {
		Boolean deleteCourseById = courseService.deleteCourseById(courseId);
		if (deleteCourseById != null)
			return ResponseEntity.ok(new ApiResponse(Boolean.TRUE, AppConstants.DELETE_SUCCESS, HttpStatus.OK));
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ApiResponse(Boolean.FALSE, AppConstants.SOMETHING_WENT_WRONG , HttpStatus.INTERNAL_SERVER_ERROR));
	}

	@GetMapping("/getAllNonStarterCourses")
	public ResponseEntity<?> getAllNonStarterCourses(){
		return courseService.getAllNonStarterCourses();
	}
	
	@PutMapping("/studentUpgradeCourse")
	public ResponseEntity<?> studentUpgradeCourse(@RequestParam("studentId") Integer studnetId
			,@RequestParam("courseId") Integer courseId){
		Map<String, Object> response = courseService.studentUpgradeCourse(studnetId,courseId);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/getCourseProgress")
	public ResponseEntity<?> getCourseProgress(@RequestParam("studentId") Integer studentId)
	{
		return courseService.getCourseProgress(studentId);
	}
	
	@GetMapping("/getCoureWithBatchesAndSubjects")
   public ResponseEntity<?>getCoureWithBatchesAndSubjects(@RequestParam("courseId")Integer courseId){
		 return  courseService.getCoureWithBatchesAndSubjects(courseId);
	}
	
	@GetMapping("/getAllCourseApi")
	public ResponseEntity<?> getAllStarterCourses(){
		return courseService.getAllStarterCourses();
	}
	
	@GetMapping("/getAllCourseForStudent")
	public ResponseEntity<?>getAllCourseForStudent(@RequestParam Integer studentId){
		return courseService.getAllCourseForStudent(studentId);
	}
}
