package com.cico.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Announcement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long announcementId;
	
	@Column(columnDefinition = "mediumtext")
	private String title;
	
	@Column(columnDefinition = "longtext")
	private String message;
	
	private LocalDateTime date;
	
	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private List<Course> course;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn
	private MessageSeenBy seenBy;
	
	@ManyToMany
	private List<Student> students;
	
}
