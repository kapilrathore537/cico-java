package com.cico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cico.payload.AnnouncementRequest;
import com.cico.service.IAnnouncementService;

@CrossOrigin("*")
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

	@Autowired
	private IAnnouncementService announcementService;
	
	@PostMapping("/publishAnnouncement")
	public ResponseEntity<?> publishAnnouncement(@RequestBody AnnouncementRequest announcementRequest){
		return announcementService.publishAnnouncement(announcementRequest);
	}
	
	@GetMapping("/getAllAnnouncement")
	public ResponseEntity<?> getAllPublishedAnnouncement(@RequestParam(value = "page",required = false)Integer page , @RequestParam(value = "size",required = false)Integer size){
		return announcementService.getAllPublishedAnnouncement(page,size);
	}
	
	@PostMapping("/seenAnnouncement")
	public ResponseEntity<?> seenAnnouncement(@RequestParam("announcementId") Long announcementId,@RequestParam("studentId") Integer studentId){
		return announcementService.seenAnnouncement(announcementId,studentId);
	}
	
	@GetMapping("/getAnnouncementForStudent")
	public ResponseEntity<?> getAnnouncementForStudent(@RequestParam("studentId") Integer studentId){
		return announcementService.getAnnouncementForStudent(studentId);
	}
	
	@GetMapping("/getNotificationCountForStudent")
	public ResponseEntity<?> countUnseenNotificationForStudent(@RequestParam("studentId") Integer studentId){
		return announcementService.countUnseenNotificationForStudent(studentId);
	}
}
