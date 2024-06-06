package com.cico.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DiscusssionForm {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private LocalDateTime createdDate;
	@Column(columnDefinition = "longtext")
	private String Content;
	private String audioFile;
	private String file;
	@OneToOne
	@JoinColumn
	private Student student;
	@OneToMany
	@JoinColumn
	private List<DiscussionFormComment>comments = new ArrayList<>();
	@OneToMany
	@JoinColumn
	private List<Likes>likes;
}
