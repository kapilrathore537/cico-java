package com.cico.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentSubmissionRequest {

	private Long assignmentId;
	private Long taskId;
	private Integer studentId;
	private String description;
}
