package com.cico.service;

import org.springframework.http.ResponseEntity;

import com.cico.payload.AnnouncementRequest;

public interface IAnnouncementService {

	ResponseEntity<?> publishAnnouncement(AnnouncementRequest announcementRequest);

	ResponseEntity<?> getAllPublishedAnnouncement(Integer page, Integer size);

	ResponseEntity<?> seenAnnouncement(Long announcementId, Integer studentId);

	ResponseEntity<?> getAnnouncementForStudent(Integer studentId);

	ResponseEntity<?> countUnseenNotificationForStudent(Integer studentId);

}
