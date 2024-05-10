package com.cico.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(value = Include.NON_NULL)
@NoArgsConstructor
public class TechnologyStackResponse {
	private Integer id;
	private String imageName;
	private String technologyName;
	public TechnologyStackResponse(Integer id, String imageName, String technologyName) {
		super();
		this.id = id;
		this.imageName = imageName;
		this.technologyName = technologyName;
	}

}
