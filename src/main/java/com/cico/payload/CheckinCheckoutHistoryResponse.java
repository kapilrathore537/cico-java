package com.cico.payload;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class CheckinCheckoutHistoryResponse {
	Integer attendanceId;
	LocalDate checkInDate;
	LocalTime checkInTime;
	String checkInImage;
	LocalDate checkOutDate;
	LocalTime checkOutTime;
	String checkOutImage;
	Long workingHour;
	String workReport;
	String attachment;

}

