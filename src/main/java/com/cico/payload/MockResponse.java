package com.cico.payload;

import java.time.LocalDate;

import lombok.Data;

@Data
public class MockResponse {

	private LocalDate mockDate;
	private Boolean isMock;
	private String mockPerson;
}
