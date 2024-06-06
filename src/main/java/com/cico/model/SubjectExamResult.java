package com.cico.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SubjectExamResult extends ChapterExamResult {

//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Integer id;
//	private Integer correcteQuestions;
//	private Integer wrongQuestions;
//	private Integer notSelectedQuestions;
//

	private Integer subjectExamId;
	@JsonIgnore
	@OneToOne
	@JoinColumn
	private Subject subject;
//	private Student student;
//	private Integer scoreGet;
//	public Integer totalQuestion;
//
	@ElementCollection
	@CollectionTable
	private List<Integer> randomQuestoinList = new ArrayList<>();
}
