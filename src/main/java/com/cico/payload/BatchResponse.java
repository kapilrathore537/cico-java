package com.cico.payload;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class BatchResponse {

	private Integer batchId;
	private String batchName;
	private LocalDate batchStartDate;
	private LocalTime batchTiming;
	private String batchDetails;
	private SubjectResponse subject;
}