package com.cico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cico.payload.AddExamRequest;
import com.cico.payload.ExamRequest;
import com.cico.service.IExamService;

@RestController
@RequestMapping("/exam")
@CrossOrigin("*")
public class ExamController {

	@Autowired
	private IExamService examService;

//	@PostMapping("addSubjectScheduleExam")
//	public ResponseEntity<?> addSubjectScheduleExam(@RequestParam("examName") String examName,) {
//		examService.addSubjectScheduleExam();	
//		return ResponseEntity.ok("");
//	}

//	@PostMapping("/addQuestionsToExam")
//	public ResponseEntity<String> addQuestionsToExam(@RequestParam("examId") Integer examId,
//			@RequestParam("examId") String question, @RequestParam("options") List<String> options,
//			@RequestParam(name = "image", required = false) MultipartFile image) {
//		examService.addQuestionsToExam(examId, question, options, image);
//		return ResponseEntity.ok("Questions Added");
//	}
//
//	@PutMapping("/updateExam")
//	public ResponseEntity<String> updateExam(@RequestBody Exam exam) {
//		examService.updateExam(exam);
//		return ResponseEntity.ok("Exam Updated");
//	}
//
//	@GetMapping("/getExamById")
//	public ResponseEntity<Exam> getExamById(@RequestParam("examId") Integer examId) {
//		Exam exam = examService.getExamById(examId);
//		return ResponseEntity.ok(exam);
//	}
//
//	@PutMapping("/deleteExam")
//	public ResponseEntity<String> deleteExam(@RequestParam("examId") Integer examId) {
//		examService.deleteExam(examId);
//		return ResponseEntity.ok("Exam Deleted");
//	}
//
//	@PutMapping("/updateExamStatus")
//	public ResponseEntity<String> updateExamStatus(@RequestParam("examId") Integer examId) {
//		examService.updateExamStatus(examId);
//		return ResponseEntity.ok("Exam Updated");
//	}
//
//	@GetMapping("/getAllExams")
//	public ResponseEntity<List<Exam>> getAllExams() {
//		List<Exam> exams = examService.getAllExams();
//		return ResponseEntity.ok(exams);
//	}

//	@GetMapping("/getExamsByChapter")
//	public ResponseEntity<List<Exam>> getExamsByChapter(@RequestParam("chapterId") Integer chapterId) {
//		List<Exam> exams = examService.getExamsByChapter(chapterId);
//		return ResponseEntity.ok(exams);
//	}

	//////////////////////////////////////////////////////////////////////////////// chapter
	//////////////////////////////////////////////////////////////////////////////// exam
	//////////////////////////////////////////////////////////////////////////////// //////////////////////////////////////////////////////////////////////////////////////////////////

	@PostMapping("/addChapterExam")
	public ResponseEntity<?> addChapterExamResult(@RequestBody ExamRequest chapterExamResult) {
		return this.examService.addChapterExamResult(chapterExamResult);
	}

	@GetMapping("/getChapterExamResult")
	public ResponseEntity<?> getChapterExamResult(@RequestParam("resultId") Integer id) {
		return examService.getChapterExamResult(id);
	}

	@GetMapping("/getALLChapterExamResultesByChapterIdApi")
	public ResponseEntity<?> getChapterExamResultes(@RequestParam("chapterId") Integer chapterId) {
		return examService.getChapterExamResultByChaterId(chapterId);
	}

	@GetMapping("/checkExamCompleteOrNot")
	public ResponseEntity<?> checkExamCompletedOrNot(@RequestParam("chapterId") Integer chapterId,
			@RequestParam("studentId") Integer studentId) {
		return examService.getChapterExamIsCompleteOrNot(chapterId, studentId);
	}

	///////////////////////////////////////////////////////////////// subjectExam
	///////////////////////////////////////////////////////////////// //////////////////////////////////////////////////////////////////////////////////////////

	@PostMapping("/addSubjectExam")
	public ResponseEntity<?> addSubjectExam(@RequestBody AddExamRequest request) {
		return examService.addSubjectExam(request);

	}

	@DeleteMapping("/deleteSubjectExam")
	public ResponseEntity<?> deleteSubjectExam(@RequestParam("examId") Integer examId) {
		return examService.deleteSubjectExam(examId);
	}

	@GetMapping("/getALLSubjectExamResultesBySubjectId")
	public ResponseEntity<?> getSubjectExamResultes(@RequestParam("examId") Integer examId) {
		return examService.getSubjectExamResultesBySubjectId(examId);
	}

	@GetMapping("/getSubjectExamResult")
	public ResponseEntity<?> getSubjectExamResult(@RequestParam("resultId") Integer resultId) {
		return examService.getSubjectExamResult(resultId);
	}

	@PostMapping("/addSubjectExamResult")
	public ResponseEntity<?> addSubjectExamResult(@RequestBody ExamRequest chapterExamResult) {
		return this.examService.addSubjectExamResult(chapterExamResult);
	}

	@PutMapping("/updateSubjectExam")
	public ResponseEntity<?> updateSubjectExam(@RequestBody AddExamRequest request) {
		return examService.updateSubjectExam(request);
	}

	@GetMapping("getAllSubjectNormalAndScheduleExam")
	public ResponseEntity<?> getAllSubjectNormalAndScheduleExam(@RequestParam("subjectId") Integer subjectId) {
		return examService.getAllSubjectNormalAndScheduleExam(subjectId);
	}

	// student use
	@GetMapping("getAllSubjectNormalAndScheduleExamForStudent")
	public ResponseEntity<?> getAllSubjectNormalAndScheduleExamForStudent(
			@RequestParam("studentId") Integer studentId) {
		return examService.getAllSubjectNormalAndScheduleExamForStudent(studentId);
	}

	@PutMapping("changeSubjectExamStatus")
	public ResponseEntity<?> changeSubjectExamStatus(@RequestParam Integer examId) {
		return examService.changeSubjectExamStatus(examId);
	}

	@PutMapping("setSubjectExamStartStatus")
	public ResponseEntity<?> setSubjectExamStartStatus(@RequestParam Integer examId) {
		return examService.setSubjectExamStartStatus(examId);
	}

	@PutMapping("setChapterExamStartStatus")
	public ResponseEntity<?> setChapterExamStartStatus(@RequestParam Integer examId) {
		return examService.setChapterExamStartStatus(examId);
	}

}
