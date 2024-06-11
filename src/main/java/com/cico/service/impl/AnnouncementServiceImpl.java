package com.cico.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cico.exception.ResourceAlreadyExistException;
import com.cico.kafkaServices.KafkaProducerService;
import com.cico.model.Announcement;
import com.cico.model.Course;
import com.cico.model.MessageSeenBy;
import com.cico.model.Student;
import com.cico.payload.AnnouncementRequest;
import com.cico.payload.AnnouncementResponseForAdmin;
import com.cico.payload.AnnouncementStudentResponse;
import com.cico.payload.NotificationInfo;
import com.cico.payload.PageResponse;
import com.cico.repository.AnnouncementRepository;
import com.cico.repository.CourseRepository;
import com.cico.repository.StudentRepository;
import com.cico.service.IAnnouncementService;
import com.cico.util.NotificationConstant;

@Service
public class AnnouncementServiceImpl implements IAnnouncementService {

	@Autowired
	private AnnouncementRepository announcementRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private KafkaProducerService kafkaProducerService;

	@Override
	public ResponseEntity<?> publishAnnouncement(AnnouncementRequest announcementRequest) {
		List<Course> courses = courseRepository.findBycourseIdInAndIsDeletedFalse(announcementRequest.getCourseId());
		long totalStudents = studentRepository
				.findBycourseIdInAndIsActiveTrueAndIsCompletedFalse(announcementRequest.getCourseId());
		Announcement announcement = new Announcement();
		announcement.setCourse(courses);
		announcement.setDate(LocalDateTime.now());
		announcement.setMessage(announcementRequest.getMessage());
		announcement.setTitle(announcementRequest.getTitle());

		MessageSeenBy seenBy = new MessageSeenBy();
		seenBy.setSeenBy(0L);
		seenBy.setTotalStudents(totalStudents);
		announcement.setSeenBy(seenBy);
		Announcement save = announcementRepository.save(announcement);

		// fetching all the fcmId
		// sending message via kafka to firebase
		List<NotificationInfo> fcmIds = studentRepository.findAllFcmIdByCourseIds(courses);
		kafkaProducerService.sendNotification(NotificationConstant.ANNOUNCEMENT_TOPIC, fcmIds.toString());
		return new ResponseEntity<>(announcementFilter(save), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> getAllPublishedAnnouncement(Integer page, Integer size) {

		if (page != -1) {
			Page<Announcement> announcements = announcementRepository
					.findAll(PageRequest.of(page, size, Sort.by(Direction.DESC, "date")));
			PageResponse<Announcement> pageResponse = new PageResponse<>(announcements.getContent(),
					announcements.getNumber(), announcements.getSize(), announcements.getNumberOfElements(),
					announcements.getTotalPages(), announcements.isLast());

			List<AnnouncementResponseForAdmin> collect = pageResponse.getResponse().stream()
					.map(this::announcementFilterForAdmin).toList();

			return new ResponseEntity<>(collect, HttpStatus.OK);
		} else {
			List<AnnouncementResponseForAdmin> collect = announcementRepository.findAll().stream()
					.map(this::announcementFilterForAdmin).sorted((o1, o2) -> o1.getDate().compareTo(o2.getDate()))
					.toList();
			return new ResponseEntity<>(collect, HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<?> seenAnnouncement(Long announcementId, Integer studentId) {
		Announcement announcement = announcementRepository.findById(announcementId).get();
		Student student = studentRepository.findByStudentId(studentId);
		if (!announcement.getStudents().contains(student)) {
			announcement.getStudents().add(student);
			announcement.getSeenBy().setSeenBy(announcement.getSeenBy().getSeenBy() + 1);
			return new ResponseEntity<>(announcementFilter(announcementRepository.save(announcement)),
					HttpStatus.CREATED);
		} else {
			throw new ResourceAlreadyExistException("Already Seen");
		}
	}

	@Override
	public ResponseEntity<?> getAnnouncementForStudent(Integer studentId) {

		Student student = studentRepository.findById(studentId).get();

		List<AnnouncementStudentResponse> collect = announcementRepository
				.getAnnouncementForStudentByCourse(student.getCourse(), student).stream().map(this::announcementFilter)
				.toList();

		return new ResponseEntity<>(collect, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> countUnseenNotificationForStudent(Integer studentId) {
		Student student = studentRepository.findById(studentId).get();
		Long announcements = announcementRepository.countUnseenNotificationForStudent(student.getCourse(), student);
		return new ResponseEntity<>(announcements, HttpStatus.OK);
	}

	public AnnouncementStudentResponse announcementFilter(Announcement response) {
		AnnouncementStudentResponse res = new AnnouncementStudentResponse();
		res.setAnnouncementId(response.getAnnouncementId());
		res.setDate(response.getDate());
		res.setMessage(response.getMessage());
		res.setTitle(response.getTitle());
		return res;
	}

	public AnnouncementResponseForAdmin announcementFilterForAdmin(Announcement response) {
		AnnouncementResponseForAdmin res = new AnnouncementResponseForAdmin();
		res.setAnnouncementId(response.getAnnouncementId());
		res.setDate(response.getDate());
		res.setMessage(response.getMessage());
		res.setTitle(response.getTitle());
		res.setSeenBy(response.getSeenBy());
		res.setCourseName(response.getCourse().stream().map(obj -> obj.getCourseName()).collect(Collectors.toList()));
		return res;
	}

}
