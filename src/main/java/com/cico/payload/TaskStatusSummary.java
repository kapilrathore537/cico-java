package com.cico.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusSummary {
	private Long totalCount;
	private Long reviewedCount;
	private Long unreviewedCount;
}
