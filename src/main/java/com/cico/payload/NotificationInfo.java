package com.cico.payload;

import lombok.Data;

@Data
public class NotificationInfo {

	private Integer studentId;
	private String fcmId;
	private String fullName;
	private String message;
	private String title;

	public NotificationInfo(Integer studentId, String fcmId, String fullName) {
		super();
		this.studentId = studentId;
		this.fcmId = fcmId;
		this.fullName = fullName;
	}

}
