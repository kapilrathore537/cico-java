package com.cico.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequest {
	private Integer courseId;
	private String courseName;
	private String courseFees;
	private String duration;
	private String sortDescription;
	private Integer technologyStack;
	private List<Integer>subjectIds;
	private Boolean isStarterCourse;
}
