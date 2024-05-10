package com.cico.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class SubjectResponse {
	private Integer subjectId;
	private String subjectName;
	private Boolean isDeleted;
	private Boolean isActive;
	public Long chapterCount;
	public Long chapterCompleted;
	private TechnologyStackResponse technologyStack = new TechnologyStackResponse();

	public SubjectResponse(Integer subjectId, String subjectName, Integer technologyId, String technologyName,
			String imageName) {
		this.subjectId = subjectId;
		this.subjectName = subjectName;
		this.technologyStack.setId(technologyId);
		this.technologyStack.setTechnologyName(technologyName);
		this.technologyStack.setImageName(imageName);
	}

}
