package com.cico.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cico.exception.ResourceAlreadyExistException;
import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Chapter;
import com.cico.model.Course;
import com.cico.model.Subject;
import com.cico.payload.ChapterResponse;
import com.cico.payload.SubjectResponse;
import com.cico.payload.TechnologyStackResponse;
import com.cico.repository.ChapterCompletedRepository;
import com.cico.repository.CourseRepository;
import com.cico.repository.StudentRepository;
import com.cico.repository.SubjectRepository;
import com.cico.repository.TechnologyStackRepository;
import com.cico.service.ISubjectService;
import com.cico.util.AppConstants;

@Service
public class SubjectServiceImpl implements ISubjectService {

	@Autowired
	private SubjectRepository subRepo;

	@Autowired
	private TechnologyStackRepository technologyStackRepository;

	@Autowired
	ChapterCompletedRepository chapterCompletedRepository;

	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private CourseRepository courseRepository;

	public ResponseEntity<?> addSubject(String subjectName, Integer imageId) {
		Map<String, Object> response = new HashMap<>();
		Subject subject = subRepo.findBySubjectNameAndIsDeleted(subjectName.trim());
		if (Objects.nonNull(subject))
			throw new ResourceAlreadyExistException("Subject already exist");

		subject = new Subject();
		subject.setSubjectName(subjectName.trim());
		subject.setTechnologyStack(technologyStackRepository.findById(imageId).get());

		Subject save = subRepo.save(subject);

		SubjectResponse res = new SubjectResponse();
		res.setSubjectId(save.getSubjectId());
		res.setSubjectName(save.getSubjectName());
		TechnologyStackResponse techres = new TechnologyStackResponse();
		techres.setId(save.getTechnologyStack().getId());
		techres.setImageName(save.getTechnologyStack().getImageName());
		techres.setTechnologyName(save.getTechnologyStack().getTechnologyName());

		res.setTechnologyStack(techres);
		if (Objects.nonNull(save)) {
			response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
			response.put("subject", res);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}
		response.put(AppConstants.MESSAGE, AppConstants.FAILED);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public void addChapterToSubject(Integer subjectId, String chapterName) {
//		Subject subject = subRepo.findBySubjectIdAndIsDeleted(subjectId)
//				.orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
//
//		List<Chapter> chapters = subject.getChapters();
//
//		for (Chapter chapter : chapters) {
//			if (chapter.getChapterName().trim().equals(chapterName.trim()))
//				throw new ResourceAlreadyExistException(
//						"Chapter: " + chapterName + " already exist in the Subject " + subject.getSubjectName());
//		}
//		Chapter obj = new Chapter();
//		obj.setChapterName(chapterName);
//		subject.getChapters().add(obj);
//		subRepo.save(subject);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public ResponseEntity<?> updateSubject(SubjectResponse subjectResponse) {

		Subject subject = subRepo.findBySubjectIdAndIsDeleted(subjectResponse.getSubjectId())
				.orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

		Subject sub = subRepo.findBySubjectNameAndIsDeleted(subjectResponse.getSubjectName().trim());

		if (sub != null && sub.getSubjectId() != subject.getSubjectId()) {
			throw new ResourceAlreadyExistException("Subject Already Present With This Name");
		}


		if (subject != null) {
			subject.setSubjectName(subjectResponse.getSubjectName());
			subject.setTechnologyStack(
					technologyStackRepository.findById(subjectResponse.getTechnologyStack().getId()).get());
		}

		Subject save = subRepo.save(subject);

		SubjectResponse response = new SubjectResponse();
		response.setSubjectName(save.getSubjectName());
		response.setChapterCount((long) save.getChapters().size());
		response.setSubjectId(save.getSubjectId());

		TechnologyStackResponse technologyStackResponse = new TechnologyStackResponse();
		technologyStackResponse.setImageName(save.getTechnologyStack().getImageName());
		technologyStackResponse.setTechnologyName(save.getTechnologyStack().getTechnologyName());
		technologyStackResponse.setId(save.getTechnologyStack().getId());
		response.setTechnologyStack(technologyStackResponse);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public Map<String, Object> getSubjectById(Integer subjectId) {
		Subject subject = subRepo.findBySubjectIdAndIsDeleted(subjectId)
				.orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
		subject.setChapters(
				subject.getChapters().stream().filter(obj -> obj.getIsDeleted() != true).collect(Collectors.toList()));

		List<Chapter> chapters = subject.getChapters();
		long completedCount = chapters.stream().filter(Chapter::getIsCompleted).count();
		Map<String, Object> map = new HashMap<>();
		map.put("subject", subject);
		map.put("Chapter Count", subject.getChapters().size());
		map.put("Completed Chapter Count", completedCount);
		return map;
	}

	@Override
	public void deleteSubject(Integer subjectId) {
		Subject subject = subRepo.findBySubjectIdAndIsDeleted(subjectId)
				.orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
		subject.setIsDeleted(true);
		subRepo.save(subject);
	}

	@Override
	public void updateSubjectStatus(Integer subjectId) {
		Subject subject = subRepo.findBySubjectIdAndIsDeleted(subjectId)
				.orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

		if (subject.getIsActive().equals(true))
			subject.setIsActive(false);
		else
			subject.setIsActive(true);

		subRepo.save(subject);
	}

	@Override
	public List<SubjectResponse> getAllSubjects() {

		List<Object[]> allSubject = subRepo.findByIsDeletedFalse();
		List<SubjectResponse> list = new ArrayList<>();

		if (allSubject.isEmpty())
			new ResourceNotFoundException("No subject available");
		for (Object[] row : allSubject) {
			SubjectResponse response = new SubjectResponse();
			response.setSubjectId((Integer) row[0]);
			response.setSubjectName((String) row[1]);
			response.setChapterCount((Long) row[4]);

			TechnologyStackResponse technologyStackResponse = new TechnologyStackResponse();
			technologyStackResponse.setImageName((String) row[3]);
			technologyStackResponse.setTechnologyName((String) row[2]);
			technologyStackResponse.setId((Integer) row[5]);
			response.setTechnologyStack(technologyStackResponse);

			list.add(response);
		}
		return list;

	}

	@Override
	public List<SubjectResponse> getAllSubjectsWithChapterCompletedStatus(Integer studentId) {

		Course course = studentRepository.findById(studentId).get().getCourse();

		List<Subject> subjects = courseRepository.findByCourseId(course.getCourseId()).get().getSubjects();

		List<Subject> list = subjects.stream().filter(obj -> obj.getIsDeleted() == false).collect(Collectors.toList());

		List<SubjectResponse> responseSend = new ArrayList<>();

		for (Subject s : list) {

			SubjectResponse response = new SubjectResponse();
			response.setChapterCount((long) (s.getChapters().stream().filter(obj -> obj.getIsDeleted() == false)
					.collect(Collectors.toList()).size()));
			TechnologyStackResponse stackResponse = new TechnologyStackResponse();
			stackResponse.setId(s.getTechnologyStack().getId());
			stackResponse.setImageName(s.getTechnologyStack().getImageName());
			stackResponse.setTechnologyName(s.getTechnologyStack().getTechnologyName());
			response.setTechnologyStack(stackResponse);
			response.setSubjectId(s.getSubjectId());
			response.setSubjectName(s.getSubjectName());
			response.setChapterCompleted(
					chapterCompletedRepository.countBySubjectIdAndStudentId(s.getSubjectId(), studentId));
			responseSend.add(response);

		}

		if (subjects.isEmpty())
			new ResourceNotFoundException("No subject available");

		return responseSend;

	}

	@Override
	public ResponseEntity<?> getAllChapterWithSubjectId(Integer subjectId) {

		List<Object[]> allChapterWithSubjectId = subRepo.getAllChapterWithSubjectId(subjectId);
		Map<String, Object> response = new HashMap<>();

		if (!allChapterWithSubjectId.isEmpty() && allChapterWithSubjectId.get(0)[3] != (null)) {
			List<ChapterResponse> chapterResponses = new ArrayList<>();
			for (Object[] row : allChapterWithSubjectId) {
				ChapterResponse chapterResponse = new ChapterResponse();
				chapterResponse.setChapterId((Integer) row[3]);
				chapterResponse.setChapterName((String) row[4]);
				chapterResponse.setChapterImage((String) row[1]);
				chapterResponse.setSubjectId(subjectId);
				chapterResponse.setSubjectName((String) row[2]);
				chapterResponses.add(chapterResponse);
			}
			response.put(AppConstants.MESSAGE, AppConstants.DATA_FOUND);
			response.put("chapters", chapterResponses);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			response.put(AppConstants.MESSAGE, AppConstants.NO_DATA_FOUND);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}

	@Override
	public ResponseEntity<?> getAllSubjectsByCourseId(Integer courseId) {
		List<SubjectResponse> list = subRepo.getAllSubjectByCourseId(courseId);
		Map<String, Object> response = new HashMap<>();
		if (list.isEmpty()) {
			response.put(AppConstants.MESSAGE, AppConstants.NO_DATA_FOUND);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		response.put(AppConstants.MESSAGE, AppConstants.DATA_FOUND);
		response.put("subjects", list);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAllChapterWithSubjectIdAndStudentId(Integer subjectId, Integer studentId) {

		List<Object[]> allChapterWithSubjectId = subRepo.getAllChapterWithSubjectIdAndStudentId(subjectId, studentId);
		Map<String, Object> response = new HashMap<>();
		if (!allChapterWithSubjectId.isEmpty() && allChapterWithSubjectId.get(0)[3] != null) {
			List<ChapterResponse> chapterResponses = new ArrayList<>();
			for (Object[] row : allChapterWithSubjectId) {
				ChapterResponse chapterResponse = new ChapterResponse();
				chapterResponse.setChapterId((Integer) row[3]);
				chapterResponse.setChapterName((String) row[4]);
				chapterResponse.setChapterImage((String) row[1]);
				chapterResponse.setScoreGet((Integer) row[5]);
				chapterResponses.add(chapterResponse);
			}
			response.put(AppConstants.MESSAGE, AppConstants.DATA_FOUND);
			response.put("chapters", chapterResponses);
		}
		if (!allChapterWithSubjectId.isEmpty())
			response.put("subjectName", (String) allChapterWithSubjectId.get(0)[2]);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public Subject checkSubjectIsPresent(Integer subjectId) {
		return subRepo.findById(subjectId).orElseThrow(() -> new ResourceNotFoundException("Subject not found!!"));
	}

}
