package com.cico.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(value = Include.NON_NULL)
@NoArgsConstructor

public class AssignmentAndTaskSubmission {

	private Long assignmentId;
	private String taskTitle;
	private Long taskId;
	private Long totalSubmitted;
	private Long unReveiwed;
	private boolean status;
	private Long reveiwed;
	private Long taskCount;
	private String assignmentTitle;
	private String description;
	private Long taskNumber;

	public AssignmentAndTaskSubmission(Long taskId, Long totalSubmitted, Long unReveiwed, Long reveiwed,
			String taskTitle, Boolean status) {
		super();
		this.taskId = taskId;
		this.totalSubmitted = totalSubmitted;
		this.unReveiwed = unReveiwed;
		this.reveiwed = reveiwed;
		this.taskTitle = taskTitle;
		this.status = status;

	}

	public AssignmentAndTaskSubmission(Long taskId, Long totalSubmitted, Long unReveiwed, Long reveiwed, Long taskCount,
			String taskTitle, Long assignmentId, String assignmentTitle, Boolean status, Long taskNumber) {
		super();
		this.taskId = taskId;
		this.totalSubmitted = totalSubmitted;
		this.unReveiwed = unReveiwed;
		this.reveiwed = reveiwed;
		this.taskTitle = taskTitle;
		this.taskCount = taskCount;
		this.assignmentTitle = assignmentTitle;
		this.assignmentId = assignmentId;
		this.status = status;
		this.taskNumber = taskNumber;
	}

	public AssignmentAndTaskSubmission(String taskTitle, Long taskId, Long totalSubmitted, Long unReveiwed,
			Long reveiwed) {
		super();
		this.taskTitle = taskTitle;
		this.taskId = taskId;
		this.totalSubmitted = totalSubmitted;
		this.unReveiwed = unReveiwed;
		this.reveiwed = reveiwed;
	}

}
