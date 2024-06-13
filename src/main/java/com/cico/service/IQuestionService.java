package com.cico.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.cico.model.Question;

public interface IQuestionService {

	Question addQuestionToChapterExam(Integer chapterId, String questionContent, String option1, String option2,
			String option3, String option4, MultipartFile image, String correctOption);

	ResponseEntity<?> updateQuestion(Integer questionId, String questionContent, String option1, String option2,
			String option3, String option4, String correctOption, MultipartFile image, Integer examId,Integer type);

	List<Question> getAllQuestionByChapterId(Integer chapterId);

	void deleteQuestion(Integer questionId);

	void updateQuestionStatus(Integer questionId);

	Question getQuestionById(Integer questionId); 

	Question addQuestionToSubjectExam(Integer subjectId, String questionContent, String option1, String option2,
			String option3, String option4, MultipartFile image, String correctOption);

	ResponseEntity<?> getAllSubjectQuestionForTest(Integer examId,Integer studentId);

	Map<String, Object> getAllSubjectQuestionBySubjectId(Integer subjectId);

}
