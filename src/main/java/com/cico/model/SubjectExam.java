package com.cico.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.cico.util.ExamType;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
public class SubjectExam {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer examId;

	private String examName;

	private Integer score;
	private String examImage;

	@Enumerated(EnumType.STRING)
	private ExamType examType;

	private Boolean isDeleted = false;
	private Boolean isActive = false;
	private Integer examTimer;
	private Integer passingMarks;
	private Integer totalQuestionForTest;
	private LocalDate scheduleTestDate;
	private LocalTime examStartTime;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private LocalTime extraTime;
	private Boolean isStart = Boolean.FALSE;
	@JsonBackReference
	@JoinColumn
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<SubjectExamResult> results = new ArrayList<>();

}
