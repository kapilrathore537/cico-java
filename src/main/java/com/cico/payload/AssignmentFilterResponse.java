package com.cico.payload;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AssignmentFilterResponse {
	private Long id;
	private String title;
	private List<AssignmentTaskFilterReponse> taskQuestion;
	private Integer totalTaskCompleted;
	private Boolean status;
}
