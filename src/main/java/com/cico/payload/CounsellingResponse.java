package com.cico.payload;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CounsellingResponse {
	
	private LocalDate counsellingDate;
	private Boolean isCounselling;
	private String counsellingPerson;
}
