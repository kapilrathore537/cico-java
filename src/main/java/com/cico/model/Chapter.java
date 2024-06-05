package com.cico.model;

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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Chapter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer chapterId;

	@NonNull
	private String chapterName;
	// private String chapterScore;
	private String chapterImage;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinColumn
	private List<ChapterContent> chapterContent;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn
	private Exam exam;
	private Boolean isDeleted = Boolean.FALSE;
	private Boolean isActive = Boolean.TRUE;

	private Boolean isCompleted;

}
