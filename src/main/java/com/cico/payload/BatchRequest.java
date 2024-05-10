package com.cico.payload;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchRequest {
	public Integer courseId;
	private String batchName;
	private LocalDate batchStartDate;
	private LocalTime batchTiming;
	private String batchDetails;
	private Integer subjectId;
}
