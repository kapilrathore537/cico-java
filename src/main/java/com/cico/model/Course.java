package com.cico.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer courseId;
	private String courseName;
	private String courseFees;
	private String duration;
	private String sortDescription;
	private LocalDate createdDate;
	private LocalDate updatedDate;
    private Boolean isDeleted=false;
    private Boolean isStarterCourse=false;
	
    @OneToOne
    @JoinColumn
    private TechnologyStack technologyStack;
    
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Subject> subjects = new ArrayList<>();
    
   
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
    private List<Batch> batches = new ArrayList<>();
   
    
	public Course(String courseName, String courseFees, String duration, String sortDescription,
			TechnologyStack technologyStack,Boolean isStarterCourse) {
		super();
		this.courseName = courseName;
		this.courseFees = courseFees;
		this.duration = duration;
		this.sortDescription = sortDescription;
		
		this.technologyStack = technologyStack;
		this.isStarterCourse=isStarterCourse;
	}
	
}
