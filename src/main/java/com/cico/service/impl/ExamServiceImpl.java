package com.cico.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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

import com.cico.exception.ResourceAlreadyExistException;
import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Chapter;
import com.cico.model.ChapterCompleted;
import com.cico.model.ChapterExamResult;
import com.cico.model.Exam;
import com.cico.model.Question;
import com.cico.model.Student;
import com.cico.model.Subject;
import com.cico.model.SubjectExam;
import com.cico.model.SubjectExamResult;
import com.cico.payload.AddExamRequest;
import com.cico.payload.ChapterExamResultResponse;
import com.cico.payload.ExamRequest;
import com.cico.payload.ExamResultResponse;
import com.cico.payload.QuestionResponse;
import com.cico.payload.SubjectExamResponse;
import com.cico.repository.ChapterCompletedRepository;
import com.cico.repository.ChapterExamResultRepo;
import com.cico.repository.ChapterRepository;
import com.cico.repository.ExamRepo;
import com.cico.repository.StudentRepository;
import com.cico.repository.SubjectExamRepo;
import com.cico.repository.SubjectExamResultRepo;
import com.cico.repository.SubjectRepository;
import com.cico.service.IExamService;
import com.cico.util.AppConstants;
import com.cico.util.ExamType;

@Service
public class ExamServiceImpl implements IExamService {

	@Autowired
	private ExamRepo examRepo;

	@Autowired
	ChapterRepository chapterRepo;

	@Autowired
	private ChapterExamResultRepo chapterExamResultRepo;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private ChapterCompletedRepository chapterCompletedRepository;

	@Autowired
	private SubjectExamRepo subjectExamRepo;

	@Autowired
	private SubjectExamResultRepo subjectExamResultRepo;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private SubjectServiceImpl subjectServiceImpl;

	@Override
	public ResponseEntity<?> addChapterExamResult(ExamRequest chapterExamResult) {
		Student student = studentRepository.findById(chapterExamResult.getStudentId()).get();
		Chapter chapter = chapterRepo.findById(chapterExamResult.getChapterId()).get();

		Optional<ChapterExamResult> findByChapterAndStudent = chapterExamResultRepo.findByChapterAndStudent(chapter,
				student);
		if (findByChapterAndStudent.isPresent())
			throw new ResourceAlreadyExistException("Your Are Already Submited This Test");

		ChapterExamResult examResult = new ChapterExamResult();
		Map<Integer, String> review = chapterExamResult.getReview();
		int correct = 0;
		int inCorrect = 0;
		examResult.setChapter(chapter);
		examResult.setStudent(student);

		List<Question> questions = chapter.getExam().getQuestions();
		questions = questions.stream().filter(obj -> !obj.getIsDeleted()).collect(Collectors.toList());

		for (Question q : questions) {
			Integer id = q.getQuestionId();
			String correctOption = q.getCorrectOption();

			if (Objects.nonNull(review)) {
				String reviewAns = review.get(id);
				if (Objects.nonNull(reviewAns)) {
					if (review.get(id).equals(correctOption)) {
						correct++;
					} else {
						inCorrect++;
					}
				}
			}
		}
		examResult.setReview(review);
		examResult.setCorrecteQuestions(correct);
		examResult.setWrongQuestions(inCorrect);
		examResult.setNotSelectedQuestions(questions.size() - (correct + inCorrect));
		examResult.setScoreGet(correct - inCorrect);
		examResult.setTotalQuestion(questions.size());
		ChapterExamResult save = chapterExamResultRepo.save(examResult);

		ChapterCompleted chapterCompleted = new ChapterCompleted();
		chapterCompleted.setChapterId(chapterExamResult.getChapterId());
		chapterCompleted.setStudentId(chapterExamResult.getStudentId());
		chapterCompleted.setSubjectId(chapterExamResult.getSubjectId());
		chapterCompletedRepository.save(chapterCompleted);

		ExamResultResponse res = new ExamResultResponse();
		res.setCorrecteQuestions(save.getCorrecteQuestions());
		res.setId(save.getNotSelectedQuestions());
		res.setScoreGet(save.getScoreGet());
		res.setWrongQuestions(save.getWrongQuestions());
		res.setId(save.getId());

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> addSubjectExamResult(ExamRequest request) {
		Map<String, String> response = new HashMap<>();

		Student student = studentRepository.findById(request.getStudentId()).get();
		Subject subject = subjectRepository.findById(request.getSubjectId()).get();
		SubjectExam subjectExam = subjectExamRepo.findById(request.getExamId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.EXAM_NOT_FOUND));

		if (subjectExam.getExamType() == ExamType.SCHEDULEEXAM) {

			LocalDateTime scheduledDateTime = LocalDateTime.of(subjectExam.getScheduleTestDate(),
					subjectExam.getExamStartTime());
			LocalDateTime examEndTime = scheduledDateTime.plus(subjectExam.getExamTimer() + 1, ChronoUnit.MINUTES);
			LocalDateTime now = LocalDateTime.now();

			if (!now.isBefore(examEndTime) && !now.isAfter(scheduledDateTime)) {
				System.out.println("Exam submission is not allowed at this time.");
				response.put(AppConstants.MESSAGE, AppConstants.SORRY_EXAM_END);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

		}
		Optional<SubjectExamResult> result = subjectExamResultRepo.findByExamIdAndStudentId(request.getExamId(),
				student);
		if (result.isPresent())
			throw new ResourceAlreadyExistException("Your Are Already Submited This Exam");

		SubjectExamResult examResult = new SubjectExamResult();
		Map<Integer, String> review = request.getReview();
		int correct = 0;
		int inCorrect = 0;
		examResult.setSubjectExamId(request.getExamId());
		examResult.setStudent(student);

		List<Question> questions = subject.getQuestions().stream()
				.filter(obj -> request.getQuestionList().contains(obj.getQuestionId())).collect(Collectors.toList());

		subject.getChapters().stream().forEach(temp -> {
			temp.getExam().getQuestions().stream().forEach(temp1 -> {
				if (request.getQuestionList().contains(temp1.getQuestionId())) {
					questions.add(temp1);
				}
			});
		});

		for (Question q : questions) {
			Integer id = q.getQuestionId();
			String correctOption = q.getCorrectOption();

			if (Objects.nonNull(review)) {
				String reviewAns = review.get(id);
				if (Objects.nonNull(reviewAns)) {
					if (review.get(id).equals(correctOption)) {
						correct++;
					} else {
						inCorrect++;
					}
				}
			}
		}
		examResult.setSubject(subject);
		examResult.setSubjectExamId(request.getExamId());
		examResult.setRandomQuestoinList(request.getQuestionList());
		examResult.setReview(review);
		examResult.setCorrecteQuestions(correct);
		examResult.setWrongQuestions(inCorrect);
		examResult.setNotSelectedQuestions(questions.size() - (correct + inCorrect));
		examResult.setScoreGet(correct - inCorrect);
		examResult.setTotalQuestion(questions.size());
		SubjectExamResult save = subjectExamResultRepo.save(examResult);

		subjectExam.getResults().add(save);
		subjectExamRepo.save(subjectExam);

		ExamResultResponse res = new ExamResultResponse();
		res.setCorrecteQuestions(save.getCorrecteQuestions());
		res.setId(save.getNotSelectedQuestions());
		res.setScoreGet(save.getScoreGet());
		res.setWrongQuestions(save.getWrongQuestions());
		res.setId(save.getId());

		return new ResponseEntity<>(res, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> getChapterExamResult(Integer id) {

		Map<String, Object> response = new HashMap<>();

		ChapterExamResult examResult = chapterExamResultRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.NO_DATA_FOUND));
		ChapterExamResultResponse chapterExamResultResponse = new ChapterExamResultResponse();

		chapterExamResultResponse.setCorrecteQuestions(examResult.getCorrecteQuestions());
		chapterExamResultResponse.setId(examResult.getId());
		chapterExamResultResponse.setNotSelectedQuestions(examResult.getNotSelectedQuestions());
		chapterExamResultResponse.setReview(examResult.getReview());
		chapterExamResultResponse.setWrongQuestions(examResult.getWrongQuestions());
		chapterExamResultResponse.setTotalQuestion(examResult.getTotalQuestion());
		chapterExamResultResponse.setScoreGet(examResult.getScoreGet());

		List<QuestionResponse> questions = examResult.getChapter().getExam().getQuestions().stream()
				.map(obj -> questionFilter(obj)).collect(Collectors.toList());

		response.put("examResult", chapterExamResultResponse);
		response.put("questions", questions);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getSubjectExamResult(Integer resultId) {

		Map<String, Object> response = new HashMap<>();

		SubjectExamResult examResult = subjectExamResultRepo.findById(resultId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.NO_DATA_FOUND));

		List<Question> questions = examResult.getSubject().getQuestions().stream().filter(obj -> {
			boolean contains = examResult.getRandomQuestoinList().contains(obj.getQuestionId());
			return contains;
		}).collect(Collectors.toList());

		examResult.getSubject().getChapters().stream().forEach(tempChapter -> {
			tempChapter.getExam().getQuestions().stream().filter(tempQuestion -> {
				boolean contains = examResult.getRandomQuestoinList().contains(tempQuestion.getQuestionId());
				return contains;
			}).forEach(questions::add);
		});

		List<QuestionResponse> questions1 = questions.stream().map(obj -> questionFilter(obj))
				.collect(Collectors.toList());

		response.put("examResult", chapterExamResultResponseFilter(examResult));
		response.put("questions", questions1);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public ChapterExamResultResponse chapterExamResultResponseFilter(SubjectExamResult examResult) {
		ChapterExamResultResponse chapterExamResultResponse = new ChapterExamResultResponse();

		chapterExamResultResponse.setCorrecteQuestions(examResult.getCorrecteQuestions());
		chapterExamResultResponse.setId(examResult.getId());
		chapterExamResultResponse.setNotSelectedQuestions(examResult.getNotSelectedQuestions());
		chapterExamResultResponse.setReview(examResult.getReview());
		chapterExamResultResponse.setWrongQuestions(examResult.getWrongQuestions());
		chapterExamResultResponse.setTotalQuestion(examResult.getTotalQuestion());
		chapterExamResultResponse.setScoreGet(examResult.getScoreGet());

		return chapterExamResultResponse;

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

	@Override
	public ResponseEntity<?> getChapterExamIsCompleteOrNot(Integer chapterId, Integer studentId) {
		Map<String, Object> response = new HashMap<>();

		Optional<Chapter> chapterRes = chapterRepo.findById(chapterId);
		Chapter chapter = chapterRepo.findByChapterIdAndIsDeleted(chapterId, false).get();
		Student student = studentRepository.findByStudentId(studentId);
		Optional<ChapterExamResult> examResult = chapterExamResultRepo.findByChapterAndStudent(chapter, student);

		if (chapterRes.isPresent() && chapterRes.get().getExam() == null) {
			response.put(AppConstants.MESSAGE, AppConstants.EXAM_NOT_FOUND);
			response.put(AppConstants.STATUS, false);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else if (chapterRes.isPresent() && chapterRes.get().getExam() != null && examResult.isPresent()) {
			response.put(AppConstants.MESSAGE, AppConstants.DATA_FOUND);
			response.put("resultId", examResult.get().getId());
			response.put(AppConstants.STATUS, true);
		} else if (chapterRes.isPresent() && chapterRes.get().getExam() != null
				&& chapterRes.get().getExam().getQuestions().size() > 0) {
			response.put(AppConstants.MESSAGE, "takeATest");
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getChapterExamResultByChaterId(Integer chapterId) {
		Map<String, Object> response = new HashMap<>();
		List<ExamResultResponse> findAllById = chapterExamResultRepo.findAllStudentResultWithChapterId(chapterId);
		if (Objects.nonNull(findAllById)) {
			response.put("examResult", findAllById);
		} else {
			response.put(AppConstants.MESSAGE, AppConstants.NO_DATA_FOUND);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getSubjectExamResultesBySubjectId(Integer examId) {

		Map<String, Object> response = new HashMap<>();
		List<ExamResultResponse> list = subjectExamRepo.findAllStudentResultWithExamId(examId);
		if (Objects.nonNull(list)) {
			response.put("examResult", list);
		} else {
			response.put(AppConstants.MESSAGE, AppConstants.NO_DATA_FOUND);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public Exam checkChapterExamIsPresent(Integer examId) {
		Exam exam = examRepo.findById(examId).orElseThrow(() -> new ResourceNotFoundException("Exam not Found!!"));
		exam.setQuestions(exam.getQuestions().stream().filter(obj -> !obj.getIsDeleted() && obj.getIsActive())
				.collect(Collectors.toList()));
		return exam;
	}

	public SubjectExam checkSubjectExamIsPresent(Integer examId) {
		SubjectExam exam = subjectExamRepo.findById(examId)
				.orElseThrow(() -> new ResourceNotFoundException("Exam not Found!!"));
		return exam;
	}

	@Override
	public ResponseEntity<?> addSubjectExam(AddExamRequest request) {

		Map<String, Object> response = new HashMap<>();

		Subject subject = subjectServiceImpl.checkSubjectIsPresent(request.getSubjectId());
		Map<String, Object> res = new HashMap<>();
		SubjectExam exam = new SubjectExam();

		Optional<SubjectExam> isExamExist = subjectExamRepo
				.findByExamNameAndIsDeletedFalse(request.getExamName().trim());
		if (isExamExist.isPresent())
			throw new ResourceAlreadyExistException(AppConstants.EXAM_ALREADY_PRESENT_WITH_THIS_NAME);

		if (request.getScheduleTestDate() != null) {

			exam.setScheduleTestDate(request.getScheduleTestDate());
			exam.setExamStartTime(request.getExamStartTime());
			exam.setExamType(ExamType.SCHEDULEEXAM);

			// checking the date must be before or equals to current date time
			LocalDate scheduleTestDate = LocalDate.of(request.getScheduleTestDate().getYear(),
					request.getScheduleTestDate().getMonthValue(), request.getScheduleTestDate().getDayOfMonth());
			LocalTime examStartTime = LocalTime.of(request.getExamStartTime().getHour(),
					request.getExamStartTime().getMinute());

			// Directly using the atTime method
			LocalDateTime scheduledDateTime = scheduleTestDate.atTime(examStartTime);

			LocalDateTime currentDateTime = LocalDateTime.now();
			if (scheduledDateTime.isBefore(currentDateTime) || scheduledDateTime.isEqual(currentDateTime)) {
				response.put(AppConstants.MESSAGE, "Exam date time must not be before till current date and time ");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		} else {
			exam.setExamType(ExamType.NORMALEXAM);
		}

		exam.setPassingMarks(request.getPassingMarks());
		exam.setExamImage(subject.getTechnologyStack().getImageName());
		exam.setExamName(request.getExamName());
		exam.setTotalQuestionForTest(request.getTotalQuestionForTest());
		exam.setExamTimer(request.getExamTimer());
		SubjectExam savedExam = subjectExamRepo.save(exam);
		subject.getExams().add(savedExam);
		subjectRepository.save(subject);

		res.put(AppConstants.SUBJECT_EXAM, subjectExamResponseFilter(savedExam));
		res.put(AppConstants.MESSAGE, AppConstants.EXAM_ADDED_SUCCESSFULLY);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> updateSubjectExam(AddExamRequest request) {
		Map<String, Object> response = new HashMap<>();

		SubjectExam exam = checkSubjectExamIsPresent(request.getExamId());

		Optional<SubjectExam> isExamExist = subjectExamRepo.findByExamName(request.getExamName().trim());
		if (isExamExist.isPresent()) {
			if (!exam.getExamName().trim().equals(isExamExist.get().getExamName()) && isExamExist.isPresent())
				throw new ResourceAlreadyExistException(AppConstants.EXAM_ALREADY_PRESENT_WITH_THIS_NAME);
		}
		if (exam.getResults().size() != 0 || exam.getIsStart()) {
			response.put(AppConstants.MESSAGE, "Can't update this exam. Already exam is completed or live now ");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		if (exam.getExamType().equals(ExamType.SCHEDULEEXAM)) {
			if (request.getScheduleTestDate() != null)
				exam.setScheduleTestDate(request.getScheduleTestDate());
			if (request.getExamStartTime() != null)
				exam.setExamStartTime(request.getExamStartTime());

			// checking the date must be before or equals to current date time
			LocalDate scheduleTestDate = LocalDate.of(request.getScheduleTestDate().getYear(),
					request.getScheduleTestDate().getMonthValue(), request.getScheduleTestDate().getDayOfMonth());
			LocalTime examStartTime = LocalTime.of(request.getExamStartTime().getHour(),
					request.getExamStartTime().getMinute());

			LocalDateTime scheduledDateTime = scheduleTestDate.atTime(examStartTime);

			LocalDateTime currentDateTime = LocalDateTime.now();
			if (scheduledDateTime.isBefore(currentDateTime) || scheduledDateTime.isEqual(currentDateTime)) {
				response.put(AppConstants.MESSAGE, "Exam date time must not be before current date and time ");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		}

		if (request.getPassingMarks() != null)
			exam.setPassingMarks(request.getPassingMarks());
		if (request.getExamName() != null)
			exam.setExamName(request.getExamName());
		if (request.getExamTimer() != null)
			exam.setExamTimer(request.getExamTimer());
		if (request.getTotalQuestionForTest() != null)
			exam.setTotalQuestionForTest(request.getTotalQuestionForTest());

		SubjectExam examRes = subjectExamRepo.save(exam);
		response.put(AppConstants.EXAM, subjectExamResponseFilter(examRes));

		response.put(AppConstants.MESSAGE, AppConstants.UPDATE_SUCCESSFULLY);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> deleteSubjectExam(Integer examId) {

		Map<String, Object> response = new HashMap<>();
		SubjectExam exam = checkSubjectExamIsPresent(examId);
		if (!exam.getIsStart()) {
			subjectExamRepo.delete(exam);
			response.put(AppConstants.MESSAGE, AppConstants.DELETE_SUCCESS);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			response.put(AppConstants.MESSAGE, "Can't delete this exam");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}

	// for admin use
	@Override
	public ResponseEntity<?> getAllSubjectNormalAndScheduleExam(Integer subjectId) {
		Subject subject = subjectRepository.findById(subjectId)
				.orElseThrow(() -> new ResourceNotFoundException("subject not found!! "));

		Map<String, Object> response = new HashMap<>();
		List<SubjectExamResponse> normalExam = new ArrayList<>();
		List<SubjectExamResponse> scheduleExam = new ArrayList<>();
		subject.getExams().stream().forEach(obj -> {
			if (obj.getExamType().equals(ExamType.SCHEDULEEXAM)) {
				SubjectExamResponse res = subjectExamResponseFilter(obj);
				scheduleExam.add(res);
			} else {
				SubjectExamResponse res = subjectExamResponseFilter(obj);
				normalExam.add(res);
			}
		});

		response.put(AppConstants.NORMAL_EXAM, normalExam);
		response.put(AppConstants.SCHEDULE_EXAM, scheduleExam);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public SubjectExamResponse subjectExamResponseFilter(SubjectExam e) {
		SubjectExamResponse res = new SubjectExamResponse();
		res.setExamId(e.getExamId());
		res.setExamImage(e.getExamImage());
		res.setExamTimer(e.getExamTimer());
		res.setTotalQuestionForTest(e.getTotalQuestionForTest());
		res.setPassingMarks(e.getPassingMarks());
		res.setExamName(e.getExamName());
		res.setExamType(e.getExamType());
		res.setScheduleTestDate(e.getScheduleTestDate());
		res.setExamStartTime(e.getExamStartTime());
		res.setIsActive(e.getIsActive());
		res.setIsStart(e.getIsStart());
		return res;
	}

	// for student use
	@Override
	public ResponseEntity<?> getAllSubjectNormalAndScheduleExamForStudent(Integer studentId) {
		studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("student not found !! "));

		Map<String, Object> response = new HashMap<>();
		List<SubjectExamResponse> allSubjectExam = new ArrayList<>();

		allSubjectExam = subjectRepository.getAllSubjectExam(studentId);
		List<SubjectExamResponse> normalExam = new ArrayList<>();
		List<SubjectExamResponse> scheduleExam = new ArrayList<>();
		allSubjectExam.stream().forEach(obj -> {
			if (obj.getExamType().equals(ExamType.SCHEDULEEXAM)) {
				LocalDateTime scheduledDateTime = LocalDateTime.of(obj.getScheduleTestDate(), obj.getExamStartTime());
				LocalDateTime examEndTime = scheduledDateTime.plus(2, ChronoUnit.MINUTES);
				LocalDateTime now = LocalDateTime.now();

				if (now.isBefore(examEndTime)) {
					obj.setIsExamEnd(false); // Exam is not ended
					System.err.println("Exam is not ended  " + obj.getExamName());
					obj.setExtraTime(1);
				} else {
					obj.setIsExamEnd(true);
					System.err.println(" Exam has ended" + obj.getExamName());
				}
				scheduleExam.add(obj);
			} else {
				normalExam.add(obj);
			}
		});
		response.put(AppConstants.NORMAL_EXAM, normalExam);
		response.put(AppConstants.SCHEDULE_EXAM, scheduleExam);
		response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> changeSubjectExamStatus(Integer examId) {
		Map<String, Object> response = new HashMap<>();
		SubjectExam exam = checkSubjectExamIsPresent(examId);
		if (!exam.getIsStart()) {
			exam.setIsActive(!exam.getIsActive());
			subjectExamRepo.save(exam);

			response.put("isActive", exam.getIsActive());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			response.put(AppConstants.MESSAGE, "Can't inactive this exam!. Exam is scheduled or live");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> setSubjectExamStartStatus(Integer examId) {

		SubjectExam exam = checkSubjectExamIsPresent(examId);
		exam.setIsStart(true);
		SubjectExam save = subjectExamRepo.save(exam);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> setChapterExamStartStatus(Integer examId) {
		return null;
	}
}
