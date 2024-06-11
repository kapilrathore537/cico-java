package com.cico.service.impl;

import java.time.Duration;
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
import com.cico.kafkaServices.KafkaProducerService;
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
import com.cico.payload.NotificationInfo;
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
import com.cico.util.NotificationConstant;

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

	@Autowired
	private KafkaProducerService kafkaProducerService;

	@Override
	public ResponseEntity<?> addChapterExamResult(ExamRequest chapterExamResult) {
		Student student = studentRepository.findById(chapterExamResult.getStudentId()).get();
		Chapter chapter = chapterRepo.findById(chapterExamResult.getChapterId()).get();

		Optional<ChapterExamResult> findByChapterAndStudent = chapterExamResultRepo.findByChapterAndStudent(chapter,
				student);
		if (findByChapterAndStudent.isPresent())
			throw new ResourceAlreadyExistException("You have already submitted this test");

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

		// .....firebase notification .....//

		NotificationInfo fcmIds = studentRepository.findFcmIdByStudentId(student.getStudentId());
		String message = String.format("Congratulations! You have successfully completed your exam. Well done!");
		fcmIds.setMessage(message);
		fcmIds.setTitle("Exam Completed!");
		kafkaProducerService.sendNotification(NotificationConstant.COMMON_TOPIC, fcmIds.toString());
		// .....firebase notification .....//

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
			throw new ResourceAlreadyExistException("You have already submitted this test");

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

		// .....firebase notification .....//

		NotificationInfo fcmIds = studentRepository.findFcmIdByStudentId(student.getStudentId());
		String message = String.format("Congratulations! You have successfully completed your exam. Well done!");
		fcmIds.setMessage(message);
		fcmIds.setTitle("Exam Completed!");
		kafkaProducerService.sendNotification(NotificationConstant.COMMON_TOPIC, fcmIds.toString());
		// .....firebase notification .....//

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

	public LocalDateTime changeIntoLocalDateTime(LocalDate date, LocalTime time) {

		LocalDate scheduleTestDate = LocalDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
		LocalTime examStartTime = LocalTime.of(time.getHour(), time.getMinute());
		return scheduleTestDate.atTime(examStartTime);
	}

	@Override
	public ResponseEntity<?> addSubjectExam(AddExamRequest request) {

		Map<String, Object> response = new HashMap<>();

		Subject subject = subjectServiceImpl.checkSubjectIsPresent(request.getSubjectId());
		SubjectExam exam = new SubjectExam();

		Optional<SubjectExam> isExamExist = subject.getExams().stream().findFirst()
				.filter(obj -> obj.getExamName().equals(request.getExamName().trim()));

		// checking exam existance with the name;
		boolean contains = isExamExist.isPresent() && subject.getExams().contains(isExamExist.get());

		if (contains)
			throw new ResourceAlreadyExistException(AppConstants.EXAM_ALREADY_PRESENT_WITH_THIS_NAME);

		// schedule exam case
		if (request.getScheduleTestDate() != null) {
			// checking the date must not be before or equals to current date time
			LocalDateTime scheduledDateTime = changeIntoLocalDateTime(request.getScheduleTestDate(),
					request.getExamStartTime());

			LocalDateTime currentDateTime = LocalDateTime.now();
			if (scheduledDateTime.isBefore(currentDateTime) || scheduledDateTime.isEqual(currentDateTime)) {
				response.put(AppConstants.MESSAGE, "Exam date and time must be in the future");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			// ensuring!. checking exam time not under previous exam duration time
			SubjectExam latestExam = subjectExamRepo.findLatestExam();

			if (latestExam != null && subject.getExams().contains(latestExam)) {

				LocalDateTime actuallatestExamTime = changeIntoLocalDateTime(latestExam.getScheduleTestDate(),
						latestExam.getExamStartTime());
				LocalDateTime latestExamTimeWithDuration = changeIntoLocalDateTime(latestExam.getScheduleTestDate(),
						latestExam.getExamStartTime().plusMinutes(latestExam.getExamTimer()));

				LocalDateTime requestDateTime = changeIntoLocalDateTime(request.getScheduleTestDate(),
						request.getExamStartTime());

				if (requestDateTime.isAfter(actuallatestExamTime)
						&& requestDateTime.isBefore(latestExamTimeWithDuration)) {
					Duration duration = Duration.between(requestDateTime, latestExamTimeWithDuration);

					long hours = duration.toHours();
					long minutes = duration.toMinutes() % 60;

					String message = "";
					if (hours != 0)
						message = String.format(
								"Please add the exam after %d hours and %d minutes from request date and time. An another exam is already scheduled during this time. or Add after this time",
								hours, minutes);
					else
						message = String.format(
								"Please add the exam after  %d minutes  from request date and time. An another exam is already scheduled during this time. or Add after this time",
								minutes);

					response.put(AppConstants.MESSAGE, message);
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}

			}

			exam.setScheduleTestDate(request.getScheduleTestDate());
			exam.setExamStartTime(request.getExamStartTime());
			exam.setExamType(ExamType.SCHEDULEEXAM);

		} else {
			exam.setExamType(ExamType.NORMALEXAM);
		}

		exam.setPassingMarks(request.getPassingMarks());
		exam.setExamImage(subject.getTechnologyStack().getImageName());
		exam.setExamName(request.getExamName().trim());
		exam.setTotalQuestionForTest(request.getTotalQuestionForTest());
		exam.setExamTimer(request.getExamTimer());
		exam.setCreatedDate(LocalDateTime.now());
		exam.setUpdatedDate(LocalDateTime.now());
		SubjectExam savedExam = subjectExamRepo.save(exam);
		subject.getExams().add(savedExam);
		subjectRepository.save(subject);

		response.put(AppConstants.SUBJECT_EXAM, subjectExamResponseFilter(savedExam));
		response.put(AppConstants.MESSAGE, AppConstants.EXAM_ADDED_SUCCESSFULLY);
		return new ResponseEntity<>(response, HttpStatus.OK);
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
			response.put(AppConstants.MESSAGE, "Cannot update exam: It is either completed or currently live");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		if (exam.getExamType().equals(ExamType.SCHEDULEEXAM)) {

			if (request.getScheduleTestDate() != null)
				exam.setScheduleTestDate(request.getScheduleTestDate());
			if (request.getExamStartTime() != null)
				exam.setExamStartTime(request.getExamStartTime());

			// checking the date must be after or equals to current date time

			LocalDateTime scheduledDateTime = changeIntoLocalDateTime(request.getScheduleTestDate(),
					request.getExamStartTime());
			LocalDateTime currentDateTime = LocalDateTime.now();
			if (scheduledDateTime.isBefore(currentDateTime) || scheduledDateTime.isEqual(currentDateTime)) {
				response.put(AppConstants.MESSAGE, "Exam date and time cannot be in the past");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			// ensuring!. checking exam time not under previous exam duration time

			Subject subject = subjectServiceImpl.checkSubjectIsPresent(request.getSubjectId());
			SubjectExam latestExam = subjectExamRepo.findLatestExam();

			if (latestExam != null && subject.getExams().contains(latestExam)) {

				LocalDateTime actuallatestExamTime = changeIntoLocalDateTime(latestExam.getScheduleTestDate(),
						latestExam.getExamStartTime());
				LocalDateTime latestExamTimeWithDuration = changeIntoLocalDateTime(latestExam.getScheduleTestDate(),
						latestExam.getExamStartTime().plusMinutes(latestExam.getExamTimer()));

				LocalDateTime requestDateTime = changeIntoLocalDateTime(request.getScheduleTestDate(),
						request.getExamStartTime());

				if (requestDateTime.isAfter(actuallatestExamTime)
						&& requestDateTime.isBefore(latestExamTimeWithDuration)) {
					Duration duration = Duration.between(requestDateTime, latestExamTimeWithDuration);

					long hours = duration.toHours();
					long minutes = duration.toMinutes() % 60;

					String message = "";
					if (hours != 0)
						message = String.format(
								"Please add the exam after %d hours and %d minutes from request date and time. An another exam is already scheduled during this time. or Add after this time",
								hours, minutes);
					else
						message = String.format(
								"Please add the exam after  %d minutes  from request date and time. An another exam is already scheduled during this time. or Add after this time",
								minutes);

					response.put(AppConstants.MESSAGE, message);
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}

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

		exam.setUpdatedDate(LocalDateTime.now());

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
				.orElseThrow(() -> new ResourceNotFoundException("subject not found!!"));

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
				LocalDateTime examEndTime = scheduledDateTime.plus(AppConstants.EXTRA_EXAM_TIME, ChronoUnit.MINUTES);
				LocalDateTime now = LocalDateTime.now();

				if (now.isBefore(examEndTime)) {
					obj.setIsExamEnd(false); // Exam is not ended
					obj.setExtraTime(1);
				} else {
					obj.setIsExamEnd(true);
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

			// .....firebase notification .....//

			List<NotificationInfo> fcmIds = studentRepository.findAllFcmIdByExamId(examId);
			String message = String
					.format("An exam has been scheduled. Please check the details and prepare accordingly.");

			List<NotificationInfo> newlist = fcmIds.parallelStream().map(obj -> {
				obj.setMessage(message);
				obj.setTitle("Exam Scheduled!");
				return obj;
			}).toList();
			kafkaProducerService.sendNotification(NotificationConstant.COMMON_TOPIC, newlist.toString());

			// .....firebase notification .....//

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
		subjectExamRepo.save(exam);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> setChapterExamStartStatus(Integer chapterId) {

		Map<String, Object> response = new HashMap<>();
		Optional<Chapter> chapter = chapterRepo.findByChapterIdAndIsDeleted(chapterId, false);
		if (chapter.isPresent()) {

			Optional<Exam> exam = examRepo.findByExamIdAndIsDeleted(chapter.get().getExam().getExamId(), false);
			if (exam.isPresent()) {
				exam.get().setIsStarted(true);
				examRepo.save(exam.get());
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
		response.put(AppConstants.MESSAGE, "Chapter not found");
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

	}

	@Override
	public ResponseEntity<?> changeChapterExamStatus(Integer examId) {

		Optional<Exam> exam = examRepo.findByExamIdAndIsDeleted(examId, false);
		Map<String, Object> response = new HashMap<>();
		if (exam.isPresent()) {
			if (!exam.get().getIsStarted()) {

				if (exam.get().getQuestions().size() == 0) {
					response.put(AppConstants.MESSAGE, "Can't active this exam!. Add some questions here");
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}

				exam.get().setIsActive(!exam.get().getIsActive());
				examRepo.save(exam.get());

				response.put("isActive", exam.get().getIsActive());
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put(AppConstants.MESSAGE, "Can't inactive this exam!. Exam is scheduled");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		}
		return new ResponseEntity<>("NOT FOUND", HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<?> getChapterExam(Integer chapterId) {

		Map<String, Object> response = new HashMap<>();
		Optional<Chapter> chapter = chapterRepo.findByChapterIdAndIsDeleted(chapterId, false);
		if (chapter.isPresent()) {
			response.put("testQuestions", chapter.get().getExam().getQuestions().parallelStream()
					.filter(obj -> !obj.getIsDeleted()).map(this::questionFilterWithoudCorrectOprion));
			response.put("examTimer", chapter.get().getExam().getExamTimer());

			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		response.put(AppConstants.MESSAGE, "Chapter not found");
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	public QuestionResponse questionFilterWithoudCorrectOprion(Question question) {
		QuestionResponse questionResponse = new QuestionResponse();
		questionResponse.setOption1(question.getOption1());
		questionResponse.setOption2(question.getOption2());
		questionResponse.setOption3(question.getOption3());
		questionResponse.setOption4(question.getOption4());
		questionResponse.setQuestionId(question.getQuestionId());
		questionResponse.setQuestionContent(question.getQuestionContent());
		questionResponse.setQuestionImage(question.getQuestionImage());
		return questionResponse;
	}

	@Override
	public ResponseEntity<?> getSubjectExamCount(Integer studentId) {

		Map<String, Object> response = new HashMap<>();
		Long normalExamCount = subjectExamRepo.fetchSubjectExamCount(ExamType.NORMALEXAM, studentId);
		Long scheduleExamCount = subjectExamRepo.fetchSubjectExamCount(ExamType.SCHEDULEEXAM, studentId);
		Long totalNormalExamCount = subjectExamRepo.fetchTotalExamCount(studentId,ExamType.NORMALEXAM);
		Long totalScheduleExamCount = subjectExamRepo.fetchTotalExamCount(studentId,ExamType.SCHEDULEEXAM);
		response.put("normalExamCount", normalExamCount);
		response.put("scheduleExamCount", scheduleExamCount);
		response.put("totalNormalCount", totalNormalExamCount);
		response.put("totalScheduleExamCount", totalScheduleExamCount);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
