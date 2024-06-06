package com.cico.model;

import lombok.Data;

@Data
public class FirebaseNotificationMessage {
	private String image;
	private String body;
	private String title;
	private String recipientToken;
}
