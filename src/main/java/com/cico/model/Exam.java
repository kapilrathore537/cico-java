package com.cico.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Exam {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer examId;

	@Column(unique = true)
	private String examName;

	private Integer score;
	private String examImage;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn
	private List<Question> questions;

	private Boolean isDeleted = Boolean.FALSE;
	private Boolean isActive = Boolean.FALSE;
	private Integer examTimer;
	private Boolean isStarted = Boolean.FALSE;

}
