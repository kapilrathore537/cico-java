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
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assignments")
public class Assignment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String title;

	private String taskAttachment;

	@JoinColumn
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<AssignmentTaskQuestion> AssignmentQuestion = new ArrayList<>();;

	@OneToOne
	@JoinColumn
	private Course course;

	@OneToOne
	@JoinColumn
	private Subject subject;

	private Boolean isDeleted = Boolean.FALSE;
	private Boolean isActive = Boolean.TRUE;

	private LocalDateTime createdDate;
	private Boolean isNotificatioSend;

}
