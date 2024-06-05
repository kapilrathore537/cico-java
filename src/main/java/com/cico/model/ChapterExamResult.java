package com.cico.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterExamResult {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Integer correcteQuestions;
	private Integer wrongQuestions;
	private Integer notSelectedQuestions;

	@OneToOne
	@JoinColumn
	private Chapter chapter;
	@OneToOne
	@JoinColumn
	private Student student;
	private Integer scoreGet;
	public Integer totalQuestion;

	@ElementCollection
	@CollectionTable
	private Map<Integer, String> review = new HashMap<>();

}
