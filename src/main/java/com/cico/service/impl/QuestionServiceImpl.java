package com.cico.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cico.exception.ResourceAlreadyExistException;
import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Chapter;
import com.cico.model.Exam;
import com.cico.model.Question;
import com.cico.model.Subject;
import com.cico.model.SubjectExam;
import com.cico.payload.QuestionResponse;
import com.cico.repository.ChapterRepository;
import com.cico.repository.ExamRepo;
import com.cico.repository.QuestionRepo;
import com.cico.repository.SubjectExamRepo;
import com.cico.repository.SubjectRepository;
import com.cico.service.IFileService;
import com.cico.service.IQuestionService;
import com.cico.util.AppConstants;

@Service
public class QuestionServiceImpl implements IQuestionService {

	@Autowired
	private QuestionRepo questionRepo;

	@Autowired
	private IFileService fileService;

	@Autowired
	private ExamRepo examRepo;
	@Autowired
	private ChapterRepository chapterRepository;

	@Autowired
	private ChapterServiceImpl chapterServiceImpl;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private SubjectExamRepo subjectExamRepo;

	@Autowired
	private ExamServiceImpl examServiceImpl;

	@Autowired
	private SubjectServiceImpl subjectServiceImpl;

	@Override
	public Question addQuestionToChapterExam(Integer chapterId, String questionContent, String option1, String option2,
			String option3, String option4, MultipartFile image, String correctOption) {
		Question questionObj = questionRepo.findByQuestionContentAndIsDeleted(questionContent.trim(), false);

		Optional<Chapter> chapter = chapterRepository.findById(chapterId);
		if (!chapter.isPresent()) {
			throw new ResourceNotFoundException("Chapter not found ");
		}

		if (Objects.nonNull(questionObj) && chapter.get().getExam().getQuestions().contains(questionObj))
			throw new ResourceAlreadyExistException("Question already exist");

		questionObj = new Question();
		questionObj.setQuestionContent(questionContent.trim());
		questionObj.setOption1(option1.trim());
		questionObj.setOption2(option2.trim());
		questionObj.setOption3(option3.trim());
		questionObj.setOption4(option4.trim());
		questionObj.setCorrectOption(correctOption.trim());
		if (image != null) {
			questionObj.setQuestionImage(image.getOriginalFilename());
			String file = fileService.uploadFileInFolder(image, AppConstants.SUBJECT_AND_CHAPTER_IMAGES);
			questionObj.setQuestionImage(file);
		}

		Question save = questionRepo.save(questionObj);
		Exam exam = chapter.get().getExam();
		exam.getQuestions().add(save);
		exam.setScore(exam.getQuestions().size());
		exam.setExamTimer(exam.getQuestions().size());
		examRepo.save(exam);
		return save;
	}

	@Override
	public Question addQuestionToSubjectExam(Integer subjectId, String questionContent, String option1, String option2,
			String option3, String option4, MultipartFile image, String correctOption) {
		Subject subject = subjectServiceImpl.checkSubjectIsPresent(subjectId);
		Question questionObj = questionRepo.findByQuestionContentAndIsDeleted(questionContent.trim(), false);
		if (Objects.nonNull(questionObj))
			throw new ResourceAlreadyExistException("Question already exist");

		questionObj = new Question();
		questionObj.setQuestionContent(questionContent.trim());
		questionObj.setOption1(option1.trim());
		questionObj.setOption2(option2.trim());
		questionObj.setOption3(option3.trim());
		questionObj.setOption4(option4.trim());
		questionObj.setCorrectOption(correctOption.trim());
		if (image != null) {
			questionObj.setQuestionImage(image.getOriginalFilename());
			String file = fileService.uploadFileInFolder(image, AppConstants.SUBJECT_AND_CHAPTER_IMAGES);
			questionObj.setQuestionImage(file);
		}

		Question save = questionRepo.save(questionObj);

		subject.getQuestions().add(save);
		subjectRepository.save(subject);

		return save;
	}

	@Override
	public ResponseEntity<?> updateQuestion(Integer questionId, String questionContent, String option1, String option2,
			String option3, String option4, String correctOption, MultipartFile image, Integer examId, Integer type) {

		Map<String, Object> response = new HashMap<>();

		// check question is present or not
		Question question = questionRepo.findByQuestionIdAndIsDeleted(questionId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Question not found"));

		if (question.getIsSelected()) {
			response.put(AppConstants.MESSAGE, "Update failed: Already selected for exams");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		// check exam is present or not
		// type 1 for checking chapter exam question
		// type 2 for checking subject exam question
		if (type == 1) {

			Optional<Exam> exam = examRepo.findByExamIdAndIsDeleted(examId, false);
			if (exam.isEmpty())
				throw new ResourceNotFoundException("Exam not found ");

			if (exam.get().getIsStarted() || exam.get().getIsActive()) {
				response.put(AppConstants.MESSAGE,
						"Can't update the question exam are activated or question is already selected in subjecte exam");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			
			Question questionObj = questionRepo.findByQuestionContentAndIsDeleted(questionContent.trim(), false);

			if (Objects.nonNull(questionObj) && exam.get().getQuestions().contains(questionObj) &&questionObj.getQuestionId() != question.getQuestionId()) {
				throw new ResourceAlreadyExistException("Question already exist");
			}
		} else if (type == 2) {
			Optional<Subject> subject = subjectRepository.findBySubjectIdAndIsDeleted(examId);

			if (subject.isEmpty()) {
				throw new ResourceNotFoundException("Subject not found ");
			}
			Question questionObj = questionRepo.findByQuestionContentAndIsDeleted(questionContent.trim(), false);
			
			if (Objects.nonNull(questionObj) && subject.get().getQuestions().contains(questionObj)
					&& questionObj.getQuestionId() != question.getQuestionId()) {
				throw new ResourceAlreadyExistException("Question already exist");
			}

		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (questionContent != null)
			question.setQuestionContent(questionContent.trim());
		if (option1 != null)
			question.setOption1(option1.trim());
		if (option2 != null)
			question.setOption2(option2.trim());
		if (option3 != null)
			question.setOption3(option3.trim());
		if (option4 != null)
			question.setOption4(option4.trim());
		if (correctOption != null)
			question.setCorrectOption(correctOption.trim());

		if (image != null && !image.isEmpty()) {
			if (image != null) {
				question.setQuestionImage(image.getOriginalFilename());
				String file = fileService.uploadFileInFolder(image, AppConstants.SUBJECT_AND_CHAPTER_IMAGES);
				question.setQuestionImage(file);
			}
		} else {
			question.setQuestionImage(question.getQuestionImage());
		}

		Question res = questionRepo.save(question);
		QuestionResponse q = new QuestionResponse();
		q.setCorrectOption(res.getCorrectOption());
		q.setOption1(res.getOption1());
		q.setOption2(res.getOption2());
		q.setOption3(res.getOption3());
		q.setOption4(res.getOption4());
		q.setQuestionContent(res.getQuestionContent());
		q.setQuestionId(res.getQuestionId());
		q.setQuestionImage(res.getQuestionImage());

		response.put(AppConstants.MESSAGE, AppConstants.UPDATE_SUCCESSFULLY);
		response.put("question", q);

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@Override
	public List<Question> getAllQuestionByChapterId(Integer chapterId) {

		Map<String, Object> chapter = chapterServiceImpl.getChapterById(chapterId);
		Chapter chapter1 = (Chapter) chapter.get("chapter");
		return chapter1.getExam().getQuestions();
	}

	@Override
	public void deleteQuestion(Integer questionId) {
		Question question = questionRepo.findByQuestionIdAndIsDeleted(questionId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Question not found"));

		question.setIsDeleted(true);
		questionRepo.save(question);
	}

	@Override
	public void updateQuestionStatus(Integer questionId) {
		Question question = questionRepo.findByQuestionIdAndIsDeleted(questionId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Question not found"));

		if (question.getIsActive().equals(true))
			question.setIsActive(false);
		else
			question.setIsActive(true);

		questionRepo.save(question);

	}

	@Override
	public Question getQuestionById(Integer questionId) {
		return this.questionRepo.findById(questionId)
				.orElseThrow(() -> new ResourceNotFoundException("Question not found with this id " + questionId));
	}

	int questionCount = 0;

	@Override
	public Map<String, Object> getAllSubjectQuestionBySubjectId(Integer subjectId) {
		Map<String, Object> response = new HashMap<>();
		Subject subject = subjectServiceImpl.checkSubjectIsPresent(subjectId);
		subject.getChapters().stream().forEach(obj -> {
			obj.getExam().getQuestions().stream().forEach(obj1 -> {
				if (obj1.getIsActive() && !obj1.getIsDeleted()) {
					questionCount += 1;
				}
			});
		});
		response.put(AppConstants.QUESTIONS, subject.getQuestions().stream()
				.filter(obj -> !obj.getIsDeleted() && obj.getIsActive()).collect(Collectors.toList()));
		response.put("questionCount", questionCount);
		questionCount = 0;
		return response;

	}

	@Override
	public ResponseEntity<?> getAllSubjectQuestionForTest(Integer examId, Integer studentId) {

		List<Question> allQuestions = new ArrayList<>();
		List<Question> randomQuestionList = new ArrayList<>();
		Map<String, Object> response = new HashMap<>();

		// check for test given or not
		Optional<SubjectExam> isExamTaken = subjectExamRepo.findByExamIdAndStudentId(examId, studentId);
		SubjectExam exam2 = examServiceImpl.checkSubjectExamIsPresent(examId);

		if (exam2.getScheduleTestDate() == LocalDate.now()
				&& exam2.getExamStartTime().getMinute() <= LocalDateTime.now().getMinute() + 15
				&& exam2.getExamStartTime().getHour() == LocalDateTime.now().getHour())

			// return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

			if (isExamTaken.isPresent()) {
				response.put(AppConstants.MESSAGE, AppConstants.EXAM_ALREADY_GIVEN);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

		SubjectExam exam = examServiceImpl.checkSubjectExamIsPresent(examId);

		Subject subject = subjectRepository.findByExamId(examId);

		allQuestions.addAll(subject.getQuestions().parallelStream()
				.filter(obj -> !obj.getIsDeleted() && obj.getIsActive()).collect(Collectors.toList()));

		List<Chapter> chapters = subject.getChapters().parallelStream().filter(obj -> !obj.getIsDeleted())
				.collect(Collectors.toList());
		List<List<Question>> collect = chapters.parallelStream().filter(o -> !o.getIsDeleted())
				.map(obj -> obj.getExam().getQuestions()).collect(Collectors.toList());

		for (List<Question> q : collect) {
			allQuestions.addAll(q);
		}

		Random random = new Random();
		int size = Math.min(exam.getTotalQuestionForTest(), allQuestions.size());
		for (int i = 0; i < size; i++) {
			int randomIndex = random.nextInt(allQuestions.size());
			randomQuestionList.add(allQuestions.remove(randomIndex));
		}

		questionRepo.setQuestionIsSelectdTrue(randomQuestionList);
		response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
		response.put(AppConstants.QUESTIONS,
				randomQuestionList.parallelStream().map(obj -> questionFilter(obj)).collect(Collectors.toList()));
		response.put(AppConstants.TIMER, exam.getExamTimer());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public QuestionResponse questionFilter(Question question) {
		QuestionResponse q = new QuestionResponse();
		q.setOption1(question.getOption1());
		q.setOption2(question.getOption2());
		q.setOption3(question.getOption3());
		q.setOption4(question.getOption4());
		q.setQuestionContent(question.getQuestionContent());
		q.setQuestionImage(question.getQuestionImage());
		q.setQuestionId(question.getQuestionId());
		q.setIsSelected(question.getIsSelected());

		return q;
	}

}
