package com.cico.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.cico.model.FirebaseNotificationMessage;
import com.cico.payload.NotificationInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KafkaConsumerService {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private FirebaseNotificationService notificationService;

	// Annoucement constant
	private final String NOTIFICATION_GROUP_ID = "notification_group";
	private final String ANNOUNCEMENT_NOTIFICATION_MESSAGE = "New announcement";
	private final String ANNOUNCEMENT_TOPIC = "annoucement";
	private final String ANNOUCEMENT_TITLE = "Announcement";

	// Createt task constant
	private final String TASK_TITLE = "New task assign";
	private final String NEW_TASK_GROUP_ID = "task_gorup";
	private final String TASK_TOPIC = "task";
	private final String TASK_NOTIFICATION_MESSAGE = "task";

	// Task status constant
	private final String TASK_ACCPETED_STATUS_TITLE = "Task Accepted";
	private final String TASK_REJECTED_STATUS_TITLE = "Task Rejected";
	private final String TASK_REVIEWING_STATUS_TITLE = "Task Reviewing";
	private final String TASK_STATUS_GROUP_ID = "task_statis_gorup";
	private final String TASK_STATUS_TOPIC = "task_status";
	private final String TASK_STATUS_NOTIFICATION_MESSAGE = "task";

	// Create assignment status constant
	private final String ASSIGNMENT_TITLE = "New task assign";
	private final String ASSIGNMENT_GROUP_ID = "task_gorup";
	private final String ASSIGNMENT_TOPIC = "task";
	private final String ASSIGNMENT_NOTIFICATION_MESSAGE = "task";

	// sending announcement notification for all the specific course students
	@KafkaListener(topics = ANNOUNCEMENT_TOPIC, groupId = NOTIFICATION_GROUP_ID)
	public void recieveNotification(String notification) throws JsonMappingException, JsonProcessingException {

		List<NotificationInfo> notificationInfo = Arrays
				.asList(mapper.readValue(notification, NotificationInfo[].class));

		notificationInfo.forEach(obj -> {
			FirebaseNotificationMessage message = new FirebaseNotificationMessage();
			message.setBody(ANNOUNCEMENT_NOTIFICATION_MESSAGE);
			message.setImage("");
			message.setRecipientToken(obj.getFcmId());
			message.setTitle(ANNOUCEMENT_TITLE);

			notificationService.sendFBNotificationByToken(message);
		});
	}

	// Sending create task notification to all the student of specific course
	@KafkaListener(topics = TASK_TOPIC, groupId = NEW_TASK_GROUP_ID)
	public void recieveTaskNotification(String notification) throws JsonMappingException, JsonProcessingException {
		List<NotificationInfo> notificationInfo = Arrays
				.asList(mapper.readValue(notification, NotificationInfo[].class));

		notificationInfo.forEach(obj -> {
			FirebaseNotificationMessage message = new FirebaseNotificationMessage();
			message.setBody(TASK_NOTIFICATION_MESSAGE);
			message.setImage("");
			message.setRecipientToken(obj.getFcmId());
			message.setTitle(TASK_TITLE);

			notificationService.sendFBNotificationByToken(message);
		});
	}

	// Sending create assignment notification to all the student of specific course
	@KafkaListener(topics = ASSIGNMENT_TOPIC, groupId = ASSIGNMENT_GROUP_ID)
	public void recieveAssginmentNotification(String notification)
			throws JsonMappingException, JsonProcessingException {
		List<NotificationInfo> notificationInfo = Arrays
				.asList(mapper.readValue(notification, NotificationInfo[].class));

		notificationInfo.forEach(obj -> {
			FirebaseNotificationMessage message = new FirebaseNotificationMessage();
			message.setBody(ASSIGNMENT_NOTIFICATION_MESSAGE);
			message.setImage("");
			message.setRecipientToken(obj.getFcmId());
			message.setTitle(ASSIGNMENT_TITLE);

			notificationService.sendFBNotificationByToken(message);
		});
	}

	// sending task status to specific student for submission task status
	// status accepted ,rejected,Reviewing
	@KafkaListener(topics = TASK_STATUS_TOPIC, groupId = TASK_STATUS_GROUP_ID)
	public void recieveTaskStatusNotification(String notification)
			throws JsonMappingException, JsonProcessingException {
		NotificationInfo notificationInfo = mapper.readValue(notification, NotificationInfo.class);

		FirebaseNotificationMessage message = new FirebaseNotificationMessage();
		message.setBody(TASK_STATUS_NOTIFICATION_MESSAGE);
		message.setImage("");
		message.setRecipientToken(notificationInfo.getFcmId());
		message.setTitle("");

		notificationService.sendFBNotificationByToken(message);

	}

}
