package com.cico.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(value = Include.NON_NULL)
@NoArgsConstructor
public class AssignmentTaskSubmissionCounts {

	private Long taskNumber;
	private Integer taskVersion;
	private Long reveiwed;
	private Long taskCount;
	private Long totalSubmitted;
	private Long unReviewed;
	private boolean isActive;
	private Long taskId;
}
