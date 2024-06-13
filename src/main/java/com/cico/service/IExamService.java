package com.cico.service;

import org.springframework.http.ResponseEntity;

import com.cico.payload.AddExamRequest;
import com.cico.payload.ExamRequest;

public interface IExamService {

//	// void addExam(String examName);
//
//	void addQuestionsToExam(Integer examId, String question, List<String> options, MultipartFile image);
//
//	void updateExam(Exam exam);
//
//	Exam getExamById(Integer examId);
//
//	void deleteExam(Integer examId);
//
//	void updateExamStatus(Integer examId);
//
//	List<Exam> getAllExams();

	ResponseEntity<?> addChapterExamResult(ExamRequest chapterExamResult);

	ResponseEntity<?> getChapterExamResult(Integer id);

	ResponseEntity<?> getChapterExamIsCompleteOrNot(Integer chapterId, Integer studentId);

	ResponseEntity<?> getChapterExamResultByChaterId(Integer chapterId);

	ResponseEntity<?> getSubjectExamResult(Integer resultId);

	ResponseEntity<?> addSubjectExamResult(ExamRequest request);

	ResponseEntity<?> getSubjectExamResultesBySubjectId(Integer examId);

	ResponseEntity<?> addSubjectExam( AddExamRequest request);

	ResponseEntity<?> updateSubjectExam(AddExamRequest request);

	ResponseEntity<?> deleteSubjectExam(Integer examId);

	ResponseEntity<?> getAllSubjectNormalAndScheduleExam(Integer subjectId);

	ResponseEntity<?> getAllSubjectNormalAndScheduleExamForStudent(Integer studentId);

	ResponseEntity<?> changeSubjectExamStatus(Integer examId);

	ResponseEntity<?> setSubjectExamStartStatus(Integer examId);

	ResponseEntity<?> setChapterExamStartStatus(Integer chapterId);

	ResponseEntity<?> changeChapterExamStatus(Integer examId);

	ResponseEntity<?> getChapterExam(Integer chapterId);

	ResponseEntity<?> getSubjectExamCount(Integer studentId);

}
