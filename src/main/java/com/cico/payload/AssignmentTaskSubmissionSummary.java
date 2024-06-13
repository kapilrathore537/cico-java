package com.cico.payload;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(value = Include.NON_NULL)
@NoArgsConstructor
public class AssignmentTaskSubmissionSummary {

	private Long assignmentId;
	private boolean status;
	private String assignmentTitle;
	private String description;
	List<AssignmentTaskSubmissionCounts> task=new ArrayList<>();
}
