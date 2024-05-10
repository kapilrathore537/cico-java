package com.cico.payload;

import java.time.LocalDate;
import java.time.LocalTime;

import com.cico.util.ExamType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(value = Include.NON_NULL)
@NoArgsConstructor
public class SubjectExamResponse {

	private String subjectName;
	private Integer examId;
	private Integer subjectId;
	private String examImage;
	private boolean isActive;
	private Integer examTimer;
	private Integer totalQuestionForTest;
	private Integer passingMarks;
	private Integer scoreGet;
	private LocalDate scheduleTestDate;
	private Integer totalExamQuestion;
	private String examName;
	private ExamType examType;
	private Integer resultId;
	private LocalTime examStartTime;
	private Boolean isExamEnd;
	private Integer extraTime;

	public SubjectExamResponse(String examName, Integer examId, String examImage, Integer examTimer,
			Integer passingMarks, Integer scoreGet, LocalDate scheduleTestDate, Integer totalQuestionForTest,
			ExamType examType, Integer resultId, Integer subjectId, LocalTime examStartTime) {
		super();

		this.examId = examId;

		this.examImage = examImage;
		// this.isActive = isActive;
		this.examTimer = examTimer;
		this.totalQuestionForTest = totalQuestionForTest;
		this.passingMarks = passingMarks;
		this.scoreGet = scoreGet;
		this.scheduleTestDate = scheduleTestDate;
		this.examType = examType;
		this.resultId = resultId;
		this.examName = examName;
		this.examTimer = examTimer;
		this.subjectId = subjectId;
		this.examStartTime = examStartTime;
	}

}
