package com.cico.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
public class DiscussionFormComment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private LocalDateTime createdDate;
//	@JsonIgnoreProperties("student")
	@OneToOne
	@JoinColumn
	private Student student;
	@Column(columnDefinition = "longtext")
	private String content;
	private String file;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn
	private List<CommentReply> commentReply = new ArrayList<>();

}
