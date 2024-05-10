package com.cico.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TaskQuestion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long questionId;
     
	@Column(columnDefinition = "longtext")
	private String question;
    
	@ElementCollection
	@CollectionTable
	private List<String> questionImages = new ArrayList<>();;

	private String videoUrl;
	
	//private Long  taskId;

//	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
//	private List<TaskSubmission> assignmentSubmissions;
	
	private Boolean isDeleted;
}
