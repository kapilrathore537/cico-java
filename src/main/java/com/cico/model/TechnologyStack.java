package com.cico.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TechnologyStack")
public class TechnologyStack {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String imageName;
	private String technologyName;
	private Boolean isDeleted;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;

	public TechnologyStack(String technologyName, String imageName) {
		this.imageName = imageName;
		this.technologyName = technologyName;
	}
}
