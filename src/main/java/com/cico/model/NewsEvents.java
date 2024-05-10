package com.cico.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsEvents {
      
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String shortDescription;
	@Column(columnDefinition = "longtext")
	private String briefDescription;
	private String image;
	private String title;
	private Boolean isDeleted;
	private Boolean isActive;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	
}

