package com.cico.kafkaServices;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.cico.model.FirebaseNotificationMessage;
import com.cico.payload.NotificationInfo;
import com.cico.util.NotificationConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KafkaConsumerService {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private FirebaseNotificationService notificationService;

	// sending announcement notification for all the specific course students
	
	@KafkaListener(topics = NotificationConstant.ANNOUNCEMENT_TOPIC, groupId = NotificationConstant.NOTIFICATION_GROUP_ID)
	public void recieveNotification(String notification) throws JsonMappingException, JsonProcessingException {

		List<NotificationInfo> notificationInfo = Arrays
				.asList(mapper.readValue(notification, NotificationInfo[].class));

		notificationInfo.forEach(obj -> {
			FirebaseNotificationMessage message = new FirebaseNotificationMessage();
			message.setBody(NotificationConstant.ANNOUNCEMENT_NOTIFICATION_MESSAGE);
			message.setImage("");
			message.setRecipientToken(obj.getFcmId());
			message.setTitle(NotificationConstant.ANNOUCEMENT_TITLE);
			notificationService.sendFBNotificationByToken(message);
		});
	}

	// Sending create task notification to all the studenst of specific course
	
	@KafkaListener(topics = NotificationConstant.TASK_TOPIC, groupId = NotificationConstant.TASK_GROUP_ID)
	public void recieveTaskNotification(String notification) throws JsonMappingException, JsonProcessingException {
		List<NotificationInfo> notificationInfo = Arrays
				.asList(mapper.readValue(notification, NotificationInfo[].class));

		notificationInfo.forEach(obj -> {
			FirebaseNotificationMessage message = new FirebaseNotificationMessage();
			message.setBody(obj.getMessage());
			message.setImage("");
			message.setRecipientToken(obj.getFcmId());
			message.setTitle(NotificationConstant.TASK_TITLE);

			notificationService.sendFBNotificationByToken(message);
		});
	}

	// Sending create assignment notification to all the student of specific course
	
	@KafkaListener(topics = NotificationConstant.ASSIGNMENT_TOPIC, groupId = NotificationConstant.ASSIGNMENT_GROUP_ID)
	public void recieveAssginmentNotification(String notification)
			throws JsonMappingException, JsonProcessingException {
		List<NotificationInfo> notificationInfo = Arrays
				.asList(mapper.readValue(notification, NotificationInfo[].class));

		notificationInfo.forEach(obj -> {
			FirebaseNotificationMessage message = new FirebaseNotificationMessage();
			message.setBody(NotificationConstant.ASSIGNMENT_NOTIFICATION_MESSAGE);
			message.setImage("");
			message.setRecipientToken(obj.getFcmId());
			message.setTitle(NotificationConstant.ASSIGNMENT_TITLE);

			notificationService.sendFBNotificationByToken(message);
		});
	}

	// sending task status to specific student for submission task status
	// status accepted ,rejected,Reviewing
	
	@KafkaListener(topics = NotificationConstant.TASK_STATUS_TOPIC, groupId = NotificationConstant.TASK_STATUS_GROUP_ID)
	public void recieveTaskStatusNotification(String notification)
			throws JsonMappingException, JsonProcessingException {
		NotificationInfo notificationInfo = mapper.readValue(notification, NotificationInfo.class);

		FirebaseNotificationMessage message = new FirebaseNotificationMessage();
		message.setBody(notificationInfo.getMessage());
		message.setImage("");
		message.setRecipientToken(notificationInfo.getFcmId());
		message.setTitle("Task updates!");

		notificationService.sendFBNotificationByToken(message);
	}

	//.......................................................................................... COMMON METHOD ...............................................................................................//
	 
	// Common Method for task,assignment submission ,and chapter and subject exam
	// completion
	// sending submission updates to specific
	
	@KafkaListener(topics = NotificationConstant.COMMON_TOPIC, groupId = NotificationConstant.COMMON_GROUP_ID)
	public void recieveTaskAssignmentSubmissionNotification(String notification)
			throws JsonMappingException, JsonProcessingException {
		NotificationInfo notificationInfo = mapper.readValue(notification, NotificationInfo.class);

		FirebaseNotificationMessage message = new FirebaseNotificationMessage();
		message.setBody(notificationInfo.getMessage());
		message.setImage("");
		message.setRecipientToken(notificationInfo.getFcmId());
		message.setTitle(notificationInfo.getTitle());
		notificationService.sendFBNotificationByToken(message);
	}

}
