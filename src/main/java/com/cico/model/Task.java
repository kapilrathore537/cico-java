package com.cico.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long taskId;

	private String taskName;

	private String taskAttachment;

	@JoinColumn
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<TaskQuestion> TaskQuestion = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn
	private List<TaskSubmission> assignmentSubmissions;

	@OneToOne
	@JoinColumn
	private Course course;

	@OneToOne
	@JoinColumn
	private Subject subject;

	private String attachmentStatus;

	private Boolean isDeleted = Boolean.FALSE;
	private Boolean isActive = Boolean.FALSE;

	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private Boolean isLatest = Boolean.TRUE;
	private Integer taskVersion;
	private String versionType;
}
