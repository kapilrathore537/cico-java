package com.cico.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationInfo {
	
	private Integer studentId;
	private String fcmId;
	private String fullName;

}
