package com.cico.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Subject {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer subjectId;

	private String subjectName;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="subject_id")
	private List<Chapter> chapters = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="subject_id")
	private List<Question> questions = new ArrayList<>();

	@OneToOne
	private TechnologyStack technologyStack; // profile picture of subject
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="subject_id")
	private List<SubjectExam> exams = new ArrayList<>();
	private Boolean isDeleted = false;
	private Boolean isActive = true;
}
