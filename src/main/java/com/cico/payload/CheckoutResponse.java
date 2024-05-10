package com.cico.payload;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Data;

@Data
public class CheckoutResponse {
	LocalDate checkoutDate;
	LocalTime checkoutTime;
	String checkoutLat;
	String checkoutLong;
	Long workingHour;
	String checkoutImage;
	String checkoutStatus;
	LocalDateTime updatedDate;

	
	

}