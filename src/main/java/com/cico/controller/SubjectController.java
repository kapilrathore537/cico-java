package com.cico.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cico.payload.ApiResponse;
import com.cico.payload.SubjectResponse;
import com.cico.service.ISubjectService;

@RestController
@RequestMapping("/subject")
@CrossOrigin("*")
public class SubjectController {

	@Autowired
	private ISubjectService subjectService;

	@PostMapping("/addSubject")
	public ResponseEntity<?> addSubject(@RequestParam("subjectName") String subjectName,
			@RequestParam("imageId") Integer imageId) {
		return subjectService.addSubject(subjectName, imageId);
	}

	@PostMapping("/addChapterToSubject")
	public ResponseEntity<String> addChapterToSubject(@RequestParam("subjectId") Integer subjectId,
			@RequestParam("chapterName") String chapterName) {
		subjectService.addChapterToSubject(subjectId, chapterName);
		return ResponseEntity.ok("Chapter Added");
	}

	@PutMapping("/updateSubject")
	public ResponseEntity<?> updateSubject(@RequestBody SubjectResponse subject) {
		return subjectService.updateSubject(subject);

	}

	@GetMapping("/getSubjectById")
	public ResponseEntity<Map<String, Object>> getSubjectById(@RequestParam("subjectId") Integer subjectId) {
		Map<String, Object> map = subjectService.getSubjectById(subjectId);
		return ResponseEntity.ok(map);
	}

	@PutMapping("/deleteSubject")
	public ResponseEntity<String> deleteSubject(@RequestParam("subjectId") Integer subjectId) {
		subjectService.deleteSubject(subjectId);
		return ResponseEntity.ok("Subject Deleted");
	}

	@PutMapping("/updateSubjectStatus")
	public ResponseEntity<String> updateSubjectStatus(@RequestParam("subjectId") Integer subjectId) {
		subjectService.updateSubjectStatus(subjectId);
		return ResponseEntity.ok("Subject Updated");
	}

	@GetMapping("/getAllSubjects")
	public ResponseEntity<List<SubjectResponse>> getAllSubjects() {
		List<SubjectResponse> subjects = subjectService.getAllSubjects();
		return ResponseEntity.ok(subjects);
	}

	@GetMapping("/getAllSubjectsWithChapterCompletedStatus/{studentId}")
	public ResponseEntity<List<SubjectResponse>> getAllSubjects(@PathVariable("studentId") Integer studentId) {
		List<SubjectResponse> subjects = subjectService.getAllSubjectsWithChapterCompletedStatus(studentId);
		return ResponseEntity.ok(subjects);
	}

	@GetMapping("/getAllSubjectsByCourseId")
	public ResponseEntity<?> getAllSubjectsByCourseId(@RequestParam("courseId")Integer courseId){
	       return  subjectService.getAllSubjectsByCourseId(courseId);
	
	}

	@PutMapping("/deleteSubjectById")
	public ResponseEntity<ApiResponse> deleteSubjectById(@RequestParam("subjectId") Integer subjectId) {
		subjectService.deleteSubject(subjectId);
		return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Success", HttpStatus.OK), HttpStatus.OK);
	}

	@GetMapping("/getAllChapterWithSubjectId")
	public ResponseEntity<?> getAllChapterWithSubjectId(@RequestParam("subjectId") Integer subjectId) {
		return subjectService.getAllChapterWithSubjectId(subjectId);

	} 


	@GetMapping("/getAllChapterWithSubjectIdAndStudentId")
	public ResponseEntity<?> getAllChapterWithSubjectIdAndStudentId(@RequestParam("subjectId") Integer subjectId,@RequestParam("studentId") Integer studentId) {
		return subjectService.getAllChapterWithSubjectIdAndStudentId(subjectId,studentId);

	} 
}
