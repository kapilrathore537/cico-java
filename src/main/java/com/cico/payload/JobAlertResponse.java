package com.cico.payload;

import java.io.Serializable;

import com.cico.model.TechnologyStack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobAlertResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer jobId;
	private String type;
	private String  jobTitle;
	private String jobDescription;
	private String companyName;
	private String experienceRequired;
	private String technicalSkills;
	private TechnologyStack technologyStack;
	private String jobPackage;

}