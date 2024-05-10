package com.cico.payload;

import java.time.LocalDate;
import java.time.LocalTime;

import com.cico.util.ExamType;

import lombok.Data;

@Data
public class AddExamRequest {

	private ExamType examType;
	private String examName;
	private Integer examId;
	private Integer passingMarks;
	private Integer totalQuestionForTest;
	private LocalDate scheduleTestDate;
	private Integer subjectId;
	private Integer examTimer;
	private LocalTime examStartTime;
}
