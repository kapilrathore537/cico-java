package com.cico.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.cico.util.SubmissionStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class AssignmentSubmission {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long submissionId;
	@JsonIgnore
	@OneToOne
	private Student student;

	@Column(columnDefinition = "longtext")
	private String description;

	private String submitFile;

	private LocalDateTime submissionDate;

	@Enumerated(EnumType.STRING)
	private SubmissionStatus status;
	@Column(columnDefinition = "longtext")
	private String review;

}
