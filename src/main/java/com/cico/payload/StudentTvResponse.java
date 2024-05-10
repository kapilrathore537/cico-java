package com.cico.payload;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentTvResponse {

	private String userId;
	private String fullName;
	private String profilePic;
	private LocalDate checkOutDate;
	private LocalTime checkOutTime;
	private LocalDate checkInDate;
	private LocalTime checkInTime;
	private String checkOutImage;
	private String checkInImage;
	private Integer seatNumber;
	
}
