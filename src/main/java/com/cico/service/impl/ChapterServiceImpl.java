package com.cico.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cico.exception.ResourceAlreadyExistException;
import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Chapter;
import com.cico.model.ChapterContent;
import com.cico.model.Exam;
import com.cico.model.Question;
import com.cico.model.Subject;
import com.cico.payload.ChapterContentResponse;
import com.cico.payload.ChapterResponse;
import com.cico.payload.QuestionResponse;
import com.cico.repository.ChapterContentRepository;
import com.cico.repository.ChapterRepository;
import com.cico.repository.ExamRepo;
import com.cico.repository.SubjectRepository;
import com.cico.service.IChapterService;
import com.cico.util.AppConstants;

@Service
public class ChapterServiceImpl implements IChapterService {

	@Autowired
	ChapterRepository chapterRepo;

	@Autowired
	ExamRepo examRepo;

	@Autowired
	SubjectRepository subjectRepo;
	@Autowired
	ChapterContentRepository chapterContentRepository;

	@Autowired
	FileServiceImpl fileServiceImpl;

	@Override
	public ResponseEntity<?> addChapter(Integer subjectId, String chapterName, MultipartFile image) {
		Chapter obj = chapterRepo.findByChapterNameAndIsDeleted(chapterName, false);
		Map<String, Object> response = new HashMap<>();
		if (Objects.nonNull(obj)) {
			throw new ResourceAlreadyExistException("Chapter already present with name..");
		}
		Subject subject = subjectRepo.findById(subjectId).get();

		Chapter chapter = new Chapter();
		chapter.setChapterName(chapterName.trim());
		chapter.setIsCompleted(false);

		Exam exam = new Exam();
		exam.setIsDeleted(false);
		Exam exam1 = examRepo.save(exam);
		chapter.setExam(exam1);
		Chapter obj1 = chapterRepo.save(chapter);
		subject.getChapters().add(obj1);
		subjectRepo.save(subject);

		ChapterResponse chapterResponse = new ChapterResponse();
		chapterResponse.setChapterId(obj1.getChapterId());
		chapterResponse.setChapterImage(subject.getTechnologyStack().getImageName());
		chapterResponse.setSubjectName(subject.getSubjectName());
		chapterResponse.setChapterName(obj1.getChapterName());
		response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
		response.put("chapter", chapterResponse);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> updateChapter(Integer chapterId, String chapterName) {
		Map<String, Object> response = new HashMap<>();
		Chapter chapter = chapterRepo.findByChapterIdAndIsDeleted(chapterId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));

		if (chapter.getChapterName().equals(chapterName.trim())) {
			throw new ResourceAlreadyExistException("Chapter already present with name..");
		}

		chapter.setChapterName(chapterName.trim());
		chapterRepo.save(chapter);
		response.put(AppConstants.MESSAGE, AppConstants.UPDATE_SUCCESSFULLY);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public Map<String, Object> getChapterById(Integer chapterId) {
		Map<String, Object> response = new HashMap<>();
		Chapter chapter = chapterRepo.findByChapterIdAndIsDeleted(chapterId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));
		response.put("chapter", chapterFilter(chapter));
		response.put("questionLength", chapter.getExam().getQuestions().size());
		return response;
	}

	@Override
	public ResponseEntity<?> deleteChapter(Integer chapterId) {
		Chapter chapter = chapterRepo.findByChapterIdAndIsDeleted(chapterId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));
		chapter.setIsDeleted(true);
		chapterRepo.save(chapter);
		Map<String, Object> response = new HashMap<>();
		response.put(AppConstants.MESSAGE, AppConstants.DELETE_SUCCESS);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public void updateChapterStatus(Integer chapterId) {
		Chapter chapter = chapterRepo.findByChapterIdAndIsDeleted(chapterId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));

		if (chapter.getIsActive().equals(true))
			chapter.setIsActive(false);
		else
			chapter.setIsActive(true);

		chapterRepo.save(chapter);
	}

	@Override
	public List<Chapter> getAllChapters(Integer subjectId) {
//		chapterRepo.getAllChapter(subjectId);
//		Subject subject = subjectRepo.findById(subjectId).get();
//		List<Chapter> chapters = subject.getChapters();
//		if (chapters.isEmpty())
//			new ResourceNotFoundException("No chapter available");
//		return ChapterFilter(chapters);
		return null;
	}

	@Override
	public List<Chapter> getChaptersBySubject(Integer subjectId) {
		Subject subject = subjectRepo.findBySubjectIdAndIsDeleted(subjectId)
				.orElseThrow(() -> new ResourceNotFoundException("Subject Not Found"));
		List<Chapter> chapters = subject.getChapters();
		if (chapters.isEmpty())
			throw new ResourceNotFoundException("No Chapter available for the Subject: " + subject.getSubjectName());
		return ChapterFilter(subject.getChapters());
	}

	@Override
	public ResponseEntity<?> addContentToChapter(Integer chapterId, String title, String subTitle, String content) {
		Chapter chapter = chapterRepo.findById(chapterId)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));
		List<ChapterContent> chapters = chapter.getChapterContent();
		ChapterContent chapterContent = new ChapterContent();
		chapterContent.setContent(content);
		chapterContent.setSubTitle(subTitle);
		chapterContent.setTitle(title);
		chapterContent.setIsDeleted(false);
		ChapterContent save = chapterContentRepository.save(chapterContent);
		chapters.add(save);
		chapter.setChapterContent(chapters);
		chapterRepo.save(chapter);
		ChapterContentResponse contentResponse = new ChapterContentResponse();
		contentResponse.setChapterName(chapter.getChapterName());

		contentResponse.setContent(save.getContent());
		contentResponse.setId(save.getId());
		contentResponse.setTitle(save.getTitle());
		contentResponse.setSubTitle(save.getSubTitle());

		Map<String, Object> response = new HashMap<>();
		response.put(AppConstants.MESSAGE, AppConstants.CREATE_SUCCESS);
		response.put("chapterContent", contentResponse);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ChapterContent updateChapterContent(String title, String subTitle, String content, Integer contentId) {
		Optional<ChapterContent> obj = this.chapterContentRepository.findById(contentId);
		ChapterContent chapter = obj.get();
		if (title != null)
			chapter.setTitle(title);
		else
			chapter.setTitle(chapter.getTitle());
		if (subTitle != null)
			chapter.setSubTitle(subTitle);
		else
			chapter.setSubTitle(chapter.getSubTitle());
		if (content != null)
			chapter.setContent(content);
		else
			chapter.setContent(chapter.getContent());

		return this.chapterContentRepository.save(chapter);
	}

	@Override
	public ChapterContent getChapterContent(Integer chapterContentId) {
		Optional<ChapterContent> obj = this.chapterContentRepository.findById(chapterContentId);
		if (obj.isEmpty())
			throw new ResourceNotFoundException("Chapter content not found");
		return obj.get();
	}

	@Override
	public void deleteChapterContent(Integer contentId) {
		chapterContentRepository.deleteChapterContent(contentId);
	}

	public List<Chapter> ChapterFilter(List<Chapter> list) {

		return list.parallelStream().filter(obj -> {
			obj.setChapterContent(obj.getChapterContent().parallelStream().filter(obj1 -> !obj1.getIsDeleted())
					.collect(Collectors.toList()));
			if (Objects.isNull(obj.getExam()) && !obj.getExam().getIsDeleted()) {
				obj.setExam(new Exam());
			} else {
				Exam exam = obj.getExam();
				List<Question> collect = obj.getExam().getQuestions().parallelStream()
						.filter(obj1 -> !obj1.getIsDeleted()).collect(Collectors.toList());
				exam.setQuestions(collect);
				obj.setExam(exam);
			}
			return obj != null;
		}).collect(Collectors.toList());
	}

	public Chapter chapterFilter(Chapter chapter) {

		chapter.setChapterContent(chapter.getChapterContent().parallelStream().filter(obj -> !obj.getIsDeleted())
				.collect(Collectors.toList()));
		Exam exam = chapter.getExam();
		List<Question> collect = chapter.getExam().getQuestions().parallelStream().filter(obj -> !obj.getIsDeleted())
				.collect(Collectors.toList());
		exam.setQuestions(collect);
		chapter.setExam(exam);
		return chapter;
	}

	@Override
	public ResponseEntity<?> getChapterContentWithChapterId(Integer chapterId) {

		List<ChapterContentResponse> res = new ArrayList<>();

		List<Object[]> ch = chapterRepo.getChapterContentWithChapterId(chapterId);
		Map<String, Object> response = new HashMap<>();

		if (!ch.isEmpty() && ch.get(0)[1] != null) {
			for (Object[] list : ch) {
				ChapterContentResponse chapterContentResponse = new ChapterContentResponse();
				chapterContentResponse.setContent((String) list[4]);
				chapterContentResponse.setId((Integer) list[1]);
				chapterContentResponse.setTitle((String) list[2]);
				chapterContentResponse.setSubTitle((String) list[3]);
				res.add(chapterContentResponse);
			}
		}
		if (!ch.isEmpty()) {
			response.put("chapterName", ch.get(0)[0]);
		}
		response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
		response.put("chapterContent", res);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> getChaperExamQuestions(Integer chapterId) {

		Optional<Chapter> chapter = chapterRepo.findById(chapterId);
		Map<String, Object> response = new HashMap<>();

		List<Question> questions = chapter.get().getExam().getQuestions().stream()
				.filter(obj -> obj.getIsDeleted() == false).collect(Collectors.toList());

		if (questions.isEmpty()) {
			response.put(AppConstants.MESSAGE, AppConstants.NO_DATA_FOUND);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		List<QuestionResponse> list = questions.parallelStream().map(obj -> questionFilter(obj))
				.collect(Collectors.toList());

		response.put(AppConstants.MESSAGE, AppConstants.DATA_FOUND);
		response.put("questions", list);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	public QuestionResponse questionFilter(Question question) {
		QuestionResponse questionResponse = new QuestionResponse();
		questionResponse.setCorrectOption(question.getCorrectOption());
		questionResponse.setOption1(question.getOption1());
		questionResponse.setOption2(question.getOption2());
		questionResponse.setOption3(question.getOption3());
		questionResponse.setOption4(question.getOption4());
		questionResponse.setSelectedOption(question.getSelectedOption());
		questionResponse.setQuestionId(question.getQuestionId());
		questionResponse.setQuestionContent(question.getQuestionContent());
		questionResponse.setQuestionImage(question.getQuestionImage());

		return questionResponse;
	}
}
