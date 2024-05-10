package com.cico.payload;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class MispunchResponse {

	String mispunchStatus;
	LocalDate mispunchDate;
	LocalTime checkInTime;
}
