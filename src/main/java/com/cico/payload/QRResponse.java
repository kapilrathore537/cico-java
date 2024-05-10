package com.cico.payload;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QRResponse {

	private String qrData;
	private String qrKey;
	private LocalDateTime createdAt;
}
