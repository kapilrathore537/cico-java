package com.cico.kafkaServices;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cico.model.FirebaseNotificationMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FirebaseNotificationService {

	@Autowired
	private FirebaseMessaging officerFirebaseMessaging;

	public static final String NOTIFICATION_SENT_SUCCESS = "Notication Sent Successfully";
	public static final String NOTIFICATION_SENT_FAILED = "Notification Sending Failed !!!";

	public ResponseEntity<?> sendFBNotificationByToken(FirebaseNotificationMessage notificationMessage) {
		Notification notification = Notification.builder().setTitle(notificationMessage.getTitle())
				.setBody(notificationMessage.getBody()).setImage(notificationMessage.getImage()).build();

		Map<String, Object> data = new HashMap<>();
		JsonMapper mapper = new JsonMapper();
		String value = " asdfsa";
		Map<String, Object> outerData = new HashMap<>();
		try {
			data.put("data", notificationMessage);
			outerData.put("data", data);
			value = mapper.writeValueAsString(data);
			outerData.putAll(data);
			value = mapper.writeValueAsString(outerData);

			log.info("FIREBASS VALUES  ----> " + value);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		Message message = Message.builder().setToken(notificationMessage.getRecipientToken()).putData("data", value)
				.setNotification(notification).build();
		try {
			log.info("Message---->>>>" + message.toString());
			officerFirebaseMessaging.send(message);
			return new ResponseEntity<>(NOTIFICATION_SENT_SUCCESS, HttpStatus.OK);
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
			return new ResponseEntity<>(NOTIFICATION_SENT_FAILED, HttpStatus.BAD_REQUEST);
		}
	}

}
