package com.cico.payload;

import java.time.LocalDateTime;

import com.cico.util.SubmissionStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(value = Include.NON_NULL)
@NoArgsConstructor
public class AssignmentSubmissionResponse {

	private String status;
	private LocalDateTime submissionDate;
	private String applyForCourse;
	private String fullName;
	private String profilePic;
	private String title;
	private Long assignmentId;
	private Long taskId;
	private String submitFile;
	private String description;
	private Long submissionId;
	private String review;
	private String taskName;
	private Long taskNumber;

	public AssignmentSubmissionResponse(String applyForCourse, String fullName, LocalDateTime submissionDate,
			SubmissionStatus status, String profilePic, String title, String submitFile, String description,
			Long submissionId, String review, Long taskId) {
		super();
		this.status = ((SubmissionStatus) status).toString();
		this.submissionDate = submissionDate;
		this.applyForCourse = applyForCourse;
		this.fullName = fullName;
		this.profilePic = profilePic;
		this.title = title;
		this.submitFile = submitFile;
		this.description = description;
		this.submissionId = submissionId;
		this.review = review;
		this.taskId = taskId;
	}

	public AssignmentSubmissionResponse(String applyForCourse, String fullName, LocalDateTime submissionDate,
			SubmissionStatus status, String profilePic, String title, String submitFile, String description,
			Long submissionId, String review, Long assignmentId, Long taskNumber) {
		super();
		this.status = ((SubmissionStatus) status).toString();
		this.submissionDate = submissionDate;
		this.applyForCourse = applyForCourse;
		this.fullName = fullName;
		this.profilePic = profilePic;
		this.title = title;
		this.submitFile = submitFile;
		this.description = description;
		this.submissionId = submissionId;
		this.review = review;
		this.assignmentId = assignmentId;
		this.taskNumber = taskNumber;
	}

	public AssignmentSubmissionResponse(String review, SubmissionStatus status, LocalDateTime submissionDate,
			String submitFile, String description, String title, Long submissionId, Long taskNumber) {
		super();
		this.status = ((SubmissionStatus) status).toString();
		this.submissionDate = submissionDate;

		this.title = title;

		this.submitFile = submitFile;
		this.description = description;
		this.submissionId = submissionId;
		this.review = review;
		this.taskNumber = taskNumber;

	}

// ts.student.fullName ,ts.submissionDate ,ts.status,ts.student.profilePic,ts.submitFile,ts.description,ts.submissionId,ts.review ,t.question) "
	public AssignmentSubmissionResponse(String fullName, LocalDateTime submissionDate, SubmissionStatus status,
			String profilePic, String submitFile, String description, Long submissionId, String review, String title) {
		super();
		this.status = ((SubmissionStatus) status).toString();
		this.submissionDate = submissionDate;
		this.fullName = fullName;
		this.profilePic = profilePic;

		this.submitFile = submitFile;
		this.description = description;
		this.submissionId = submissionId;
		this.review = review;
		this.title = title;

	}

}
