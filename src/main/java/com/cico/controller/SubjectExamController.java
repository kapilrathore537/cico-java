package com.cico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cico.model.Question;
import com.cico.service.SubjectExamService;

@RestController
@RequestMapping("/subjectExamController")
@CrossOrigin("*")
public class SubjectExamController {

	@Autowired
	private SubjectExamService examService;

//
//	@PostMapping("/addQuestionToChapter")
//	public ResponseEntity<?> addQuestion(@RequestParam("subjectId") Integer subjectId,
//			@RequestParam("questionContent") String questionContent, @RequestParam("option1") String option1,
//			@RequestParam("option2") String option2, @RequestParam("option3") String option3,
//			@RequestParam("option4") String option4,
//			@RequestParam(name = "image", required = false) MultipartFile image,
//			@RequestParam("correctOption") String correctOption) {
//		return  examService.addQuestion(subjectId, questionContent, option1, option2, option3, option4,
//				image, correctOption);
//	}
//	@PutMapping("/updateQuestionById")
//	public ResponseEntity<?> updateQuestion(@RequestParam("questionContent") String questionContent,
//			@RequestParam("option1") String option1, @RequestParam("option2") String option2,
//			@RequestParam("option3") String option3, @RequestParam("option4") String option4,
//			@RequestParam("questionId") Integer questionId, @RequestParam("correctOption") String correctOption,
//			@RequestParam(name = "image", required = false) MultipartFile image) {
//		 return  examService.updateQuestion(questionId, questionContent, option1, option2, option3,
//				option4, correctOption, image);
//	
//	}

}
