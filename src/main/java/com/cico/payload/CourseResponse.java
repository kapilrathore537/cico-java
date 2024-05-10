package com.cico.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class CourseResponse {
	private Integer courseId;
	private String courseName;
	private String courseFees;
	private Long subjectCount;
	private Long batchesCount;
//	private String technologyImage;
	private String duration;
	private String sortDescription;
	private Boolean isStarterCourse;
	private TechnologyStackResponse technologyStack;
	private List<SubjectResponse> subjectResponse;
	private List<BatchResponse> batchResponse;
}
