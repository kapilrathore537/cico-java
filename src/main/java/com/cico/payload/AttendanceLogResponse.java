package com.cico.payload;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceLogResponse {

	private LocalDate date;
	private LocalTime checkIn;
	private LocalTime checkOut;
	private Long timeIn;
	private String status;
}
