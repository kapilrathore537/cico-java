package com.cico.payload;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class AssignmentResponse {

	private Long id;

	private String title;

	private String taskAttachment;

	private List<TaskQuestionResponse> assignmentQuestion = new ArrayList<>();;

	private LocalDateTime createdDate;
	private CourseResponse course;
	private SubjectResponse subject;
}
