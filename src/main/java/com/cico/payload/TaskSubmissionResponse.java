package com.cico.payload;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class TaskSubmissionResponse {

	private Long id;
	private String taskDescription;
	private String submittionFileName;
	private Long taskId;
	private LocalDateTime submissionDate;
	private String status;
	private String review;
	private String taskName;
	private String profilePic;
	private Integer studentId;
	private String fullName;
	private String applyForCoure;

}
