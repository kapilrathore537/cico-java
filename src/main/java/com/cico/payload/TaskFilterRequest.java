package com.cico.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskFilterRequest {

	private Integer courseId;
	private Integer subjectId;
	private Boolean status;
}
