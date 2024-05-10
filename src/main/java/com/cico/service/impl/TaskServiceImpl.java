package com.cico.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cico.config.AppUtils;
import com.cico.exception.ResourceAlreadyExistException;
import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Course;
import com.cico.model.Student;
import com.cico.model.Subject;
import com.cico.model.Task;
import com.cico.model.TaskQuestion;
import com.cico.model.TaskSubmission;
import com.cico.payload.AssignmentAndTaskSubmission;
import com.cico.payload.AssignmentSubmissionResponse;
import com.cico.payload.CourseResponse;
import com.cico.payload.SubjectResponse;
import com.cico.payload.TaskFilterRequest;
import com.cico.payload.TaskQuestionResponse;
import com.cico.payload.TaskRequest;
import com.cico.payload.TaskResponse;
import com.cico.payload.TaskStatusSummary;
import com.cico.payload.TaskSubmissionResponse;
import com.cico.repository.CourseRepository;
import com.cico.repository.StudentRepository;
import com.cico.repository.SubjectRepository;
import com.cico.repository.TaskQuestionRepository;
import com.cico.repository.TaskRepo;
import com.cico.repository.TaskSubmissionRepository;
import com.cico.service.ITaskService;
import com.cico.util.AppConstants;
import com.cico.util.SubmissionStatus;

@Service
public class TaskServiceImpl implements ITaskService {

	@Autowired
	TaskRepo taskRepo;

	@Autowired
	FileServiceImpl fileService;

	@Autowired
	CourseServiceImpl courseService;

	@Autowired
	SubjectServiceImpl subjectService;

	@Autowired
	SubjectRepository subjectRepo;

	@Autowired
	private TaskQuestionRepository taskQuestionRepository;
	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private TaskSubmissionRepository taskSubmissionRepository;
	@Autowired
	private CourseRepository courseRepository;

	@Override
	public ResponseEntity<?> createTask(TaskRequest taskRequest) {
		if (taskRepo.findByTaskName(taskRequest.getTaskName().trim()) != null)
			throw new ResourceAlreadyExistException("Task already exist");

		Map<String, Object> response = new HashMap<>();
		Task task = new Task();
		task.setAttachmentStatus(taskRequest.getAttachmentStatus());
		task.setCourse(taskRequest.getCourse());
		task.setSubject(taskRequest.getSubject());
		task.setTaskName(taskRequest.getTaskName().trim());
		task.setIsDeleted(false);
		task.setCreatedDate(LocalDateTime.now());
		Task newTask = taskRepo.save(task);
		response.put(AppConstants.MESSAGE, AppConstants.CREATE_SUCCESS);
		response.put("taskId", newTask.getTaskId());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public void updateTaskStatus(Long taskId) {
//		Task task = taskRepo.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
//
//		if (task.getIsActive().equals(true))
//			task.setIsActive(false);
//		else
//			task.setIsActive(true);
//
//		taskRepo.save(task);

	}

	@Override
	public List<Task> getFilteredTasks(TaskFilterRequest taskFilter) {
		Example<Task> example = null;

		Course course = courseRepository.findByCourseIdAndIsDeleted(taskFilter.getCourseId(), false);
		Subject subject = subjectRepo.findById(taskFilter.getSubjectId()).get();

		Task task = new Task();
		task.setCourse(course);
		task.setSubject(subject);
		task.setIsDeleted(taskFilter.getStatus());
		example = Example.of(task);
		taskRepo.findAll(example);
		return null;

	}

	@Override
	public ResponseEntity<?> getTaskById(Long taskId) {
		Task task = taskRepo.findByTaskIdAndIsDeletedFalse(taskId)
				.orElseThrow(() -> new ResourceNotFoundException("TASK NOT FOUND WITH THIS ID"));
		Map<String, Object> response = new HashMap<>();

		response.put(AppConstants.MESSAGE, AppConstants.DATA_FOUND);
		response.put("task", taskReponseFilter(task));
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	public TaskQuestionResponse taskquestionResponseFilter(TaskQuestion obj) {
		return new TaskQuestionResponse(obj.getQuestionId(), obj.getQuestion(), obj.getQuestionImages(),
				obj.getVideoUrl());
	}

	public TaskResponse taskReponseFilter(Task task) {

		TaskResponse res = new TaskResponse();
		CourseResponse cr = new CourseResponse();
		SubjectResponse sr = new SubjectResponse();

		res.setTaskId(task.getTaskId());
		res.setTaskName(task.getTaskName());
		res.setTaskQuestion(task.getTaskQuestion().parallelStream().filter(obj -> !obj.getIsDeleted())
				.map(this::taskquestionResponseFilter).collect(Collectors.toList()));

		res.setAttachmentStatus(task.getAttachmentStatus());
		res.setTaskAttachment(task.getTaskAttachment());
		cr.setCourseId(task.getCourse().getCourseId());
		cr.setCourseName(task.getCourse().getCourseName());

		sr.setSubjectId(task.getSubject().getSubjectId());
		sr.setSubjectName(task.getSubject().getSubjectName());
		res.setCreatedDate(task.getCreatedDate());
	
		res.setSubject(sr);
		res.setCourse(cr);
		return res;
	}

	@Override
	public List<Task> getAllTask() {
//		List<Task> list = taskRepo.findAll();
//		if (list.isEmpty())
//			throw new ResourceNotFoundException("Task not Found");
//		else
//			return taskre(list);
		return null;
	}

	@Override
	public ResponseEntity<?> studentTaskSubmittion(Long taskId, Integer studentId, MultipartFile file,
			String taskDescription) {
		TaskSubmission obj = taskSubmissionRepository.findByTaskIdAndStudentId(taskId, studentId);
		Optional<Task> task = taskRepo.findByTaskIdAndIsDeletedFalse(taskId);
		if (Objects.isNull(obj)) {
			TaskSubmission submittion = new TaskSubmission();
			submittion.setStudent(studentRepository.findByStudentId(studentId));
			if (Objects.nonNull(file)) {
				String f = fileService.uploadFileInFolder(file, "zip");
				submittion.setSubmittionFileName(f);
			}
			// submittion.setTaskId(taskId);
			submittion.setStatus(SubmissionStatus.Unreviewed);
			submittion.setSubmissionDate(LocalDateTime.now());
			submittion.setTaskDescription(taskDescription);
			TaskSubmission object = taskSubmissionRepository.save(submittion);
			task.get().getAssignmentSubmissions().add(object);
			taskRepo.save(task.get());
			if (Objects.nonNull(object)) {
				return new ResponseEntity<>(HttpStatus.OK);
			} else
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			throw new ResourceAlreadyExistException("ALREADY TASK SUBMITED");
		}
	}

	@Override
	public ResponseEntity<?> addQuestionInTask(String question, String videoUrl, List<MultipartFile> questionImages,
			Long taskId) {
		Optional<Task> taskOptional = taskRepo.findByTaskIdAndIsDeleted(taskId, false);

		if (taskOptional.isPresent()) {
			TaskQuestion taskQuestion = new TaskQuestion();
			taskQuestion.setQuestion(question);
			taskQuestion.setVideoUrl(videoUrl);

			if (Objects.nonNull(questionImages)) {
				questionImages.forEach((t) -> {
					String fileName = fileService.uploadFileInFolder(t, "question");
					taskQuestion.getQuestionImages().add(fileName);
				});
			}

			taskQuestion.setIsDeleted(false);
			TaskQuestion newTaskQuestion = taskQuestionRepository.save(taskQuestion);
			Task task = taskOptional.get();
			task.getTaskQuestion().add(newTaskQuestion);

			Task save = taskRepo.save(task);
			return new ResponseEntity<>(taskquestionResponseFilter(newTaskQuestion), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> addTaskAttachment(Long taskId, MultipartFile attachment) {
		Optional<Task> task = taskRepo.findByTaskIdAndIsDeleted(taskId, false);

		if (task.isPresent()) {
			if (Objects.nonNull(attachment)) {
				String fileName = fileService.uploadFileInFolder(attachment, AppConstants.TASK_ASSIGNMENT_FILES);
				task.get().setTaskAttachment(fileName);
				taskRepo.save(task.get());
			}
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<?> deleteTaskQuestion(Long questionId) {
		taskQuestionRepository.deleteTaskQuestion(questionId);
		Map<String, Object> response = new HashMap<>();
		response.put(AppConstants.MESSAGE, AppConstants.DELETE_SUCCESS);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAllSubmitedTasks(Integer courseId, Integer subjectId, SubmissionStatus status,
			Integer pageNumber, Integer pageSize) {
		Page<AssignmentSubmissionResponse> res = taskRepo.findAllSubmissionTaskWithCourseIdAndSubjectId(courseId,
				subjectId, status, PageRequest.of(pageNumber, pageSize));
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getSubmitedTaskForStudent(Integer studentId, Integer pageNumber, Integer pageSize,
			SubmissionStatus status) {
		Page<TaskSubmission> res = taskSubmissionRepository.getSubmitedTaskForStudent(studentId,
				PageRequest.of(pageNumber, pageSize), status);
		System.err.println(res.getContent());
		return new ResponseEntity<>(res, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> updateSubmitedTaskStatus(Long submissionId, String status, String review) {
		if (status.equals(SubmissionStatus.Reviewing.toString())) {
			taskSubmissionRepository.updateSubmitTaskStatus(submissionId, SubmissionStatus.Reviewing, review);
		} else if (status.equals(SubmissionStatus.Accepted.toString())) {
			taskSubmissionRepository.updateSubmitTaskStatus(submissionId, SubmissionStatus.Accepted, review);
		} else if (status.equals(SubmissionStatus.Rejected.toString())) {
			taskSubmissionRepository.updateSubmitTaskStatus(submissionId, SubmissionStatus.Rejected, review);
		}

		TaskSubmission res = taskSubmissionRepository.findBySubmissionId(submissionId);
		TaskSubmissionResponse response = new TaskSubmissionResponse();
		response.setFullName(res.getStudent().getFullName());
		response.setId(res.getId());
		response.setProfilePic(res.getStudent().getProfilePic());
		response.setReview(res.getReview());
		response.setStatus(res.getStatus().toString());
		response.setSubmissionDate(res.getSubmissionDate());
		response.setSubmittionFileName(res.getSubmittionFileName());
		response.setSubmittionFileName(res.getSubmittionFileName());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> getOverAllTaskStatusforBarChart() {
		TaskStatusSummary overAllTaskQuestionStatus = taskRepo.getOverAllTaskQuestionStatus();
		return new ResponseEntity<>(overAllTaskQuestionStatus, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAllTaskOfStudent(Integer studentId, Integer pageNumber, Integer pageSize) {

		//taskRepo.getAllTaskOfStudent(studentId, PageRequest.of(pageNumber, pageSize));
		Map<String, Object> response = new HashMap<>();
		Student student = studentRepository.findById(studentId).get();
		List<TaskResponse> collect = new ArrayList<>();
		student.getCourse().getSubjects().forEach(obj -> {
			collect.addAll(taskRepo.findBySubjectAndIsDeletedFalseAndIsActiveTrue(obj).stream()
					.filter(obj1 -> !obj1.getIsDeleted()).map(this::filterTask).map(task->taskResponseFilter(task,studentId))
					.collect(Collectors.toList()));
		});
		
	// this is for descending order
//		student.getCourse().getSubjects().forEach(obj -> {
//		    collect.addAll(
//		        taskRepo.findBySubjectAndIsDeletedFalseAndIsActiveTrue(obj)
//		            .stream()
//		            .filter(obj1 -> !obj1.getIsDeleted())
//		            .map(this::filterTask)
//		            .map(this::taskReponseFilter)
//		            .sorted(Comparator.comparing(TaskResponse::getCreatedDate).reversed()) // Sort by date in descending order
//		            .collect(Collectors.toList())
//		    );
//		});
		response.put(AppConstants.MESSAGE, collect.size() > 0 ? AppConstants.DATA_FOUND : AppConstants.NO_DATA_FOUND);
		response.put("allTask", AppUtils.convertListToPage(collect, PageRequest.of(pageNumber, pageSize)));
		 return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	
	
	public TaskResponse taskResponseFilter(Task task,Integer studentId) {
		
		return new TaskResponse(task.getTaskId(), task.getTaskName(), (Boolean) ( isTaskSubmitted(task.getTaskId(), studentId).getBody()));
	}


	
	@Override
	public ResponseEntity<?> isTaskSubmitted(Long taskId, Integer studentId) {
		TaskSubmission submission = taskSubmissionRepository.findByTaskIdAndStudentId(taskId, studentId);
		if (Objects.nonNull(submission)) {
//			if (submission.getStatus().name().equals("Rejected"))
//				return new ResponseEntity<>(false, HttpStatus.OK);
//			else
			return new ResponseEntity<>(true, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(false, HttpStatus.OK);
		}
	}

//	public List<Task> filterTasks(List<Task> list) {
//
//		List<Task> list2 = list.parallelStream().filter(o -> !o.getIsDeleted()).collect(Collectors.toList());
//		return list2.parallelStream().filter(obj -> {
//			obj.setTaskQuestion(
//					obj.getTaskQuestion().parallelStream().filter(o -> !o.getIsDeleted()).collect(Collectors.toList()));
//			return obj != null;
//		}).collect(Collectors.toList());
//	}
//
	public Task filterTask(Task task) {
		task.setTaskQuestion(task.getTaskQuestion().parallelStream().filter(obj -> !obj.getIsDeleted())
				.collect(Collectors.toList()));
		return task;
	}

	public TaskSubmissionResponse taskSubmissionResponse(TaskSubmission submission) {

		TaskSubmissionResponse response = new TaskSubmissionResponse();
		response.setId(submission.getId());
		response.setReview(submission.getReview());
		response.setStatus(submission.getStatus().toString());
		response.setTaskDescription(submission.getTaskDescription());
		response.setTaskName(submission.getTaskName());
		// response.setTaskId(submission.getTaskId());
		response.setSubmittionFileName(submission.getSubmittionFileName());
		response.setSubmissionDate(submission.getSubmissionDate());
		response.setProfilePic(submission.getStudent().getProfilePic());
		response.setStudentId(submission.getStudent().getStudentId());
		response.setFullName(submission.getStudent().getFullName());
		response.setApplyForCoure(submission.getStudent().getApplyForCourse());
		return response;
	}

	@Override
	public ResponseEntity<?> getSubmissionTaskById(Long id) {
		Map<String, Object> response = new HashMap<>();
		Optional<TaskSubmission> submission = taskSubmissionRepository.findById(id);
		if (Objects.nonNull(submission)) {
			TaskSubmissionResponse res = new TaskSubmissionResponse();
			res.setFullName(submission.get().getStudent().getFullName());
			res.setId(submission.get().getId());
			res.setReview(submission.get().getReview());
			res.setStatus((submission.get().getStatus()).toString());
			res.setProfilePic(submission.get().getStudent().getProfilePic());
			res.setSubmittionFileName(submission.get().getSubmittionFileName());
			res.setSubmissionDate(submission.get().getSubmissionDate());
			res.setSubmittionFileName(submission.get().getSubmittionFileName());
			response.put("submission", res);
			response.put(AppConstants.MESSAGE, AppConstants.DATA_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			response.put(AppConstants.MESSAGE, AppConstants.NO_DATA_FOUND);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}

	@Override
	public ResponseEntity<?> getTaskQuestion(Long questionId) {
		Optional<TaskQuestion> question = taskQuestionRepository.findById(questionId);
		Map<String, Object> response = new HashMap<>();
		if (Objects.nonNull(question)) {
			response.put(AppConstants.MESSAGE, AppConstants.DATA_FOUND);
			response.put("question", taskquestionResponseFilter(question.get()));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			response.put(AppConstants.MESSAGE, AppConstants.NO_DATA_FOUND);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<?> getAllSubmissionTaskStatusByCourseIdAndSubjectId(Integer courseId, Integer subjectId,
			Integer pageNumber, Integer pageSize) {

		Map<String, Object> response = new HashMap<>();

		Page<AssignmentAndTaskSubmission> resp = taskRepo.findAllTaskStatusWithCourseIdAndSubjectId(courseId, subjectId,
				PageRequest.of(pageNumber, pageSize, Sort.by(Direction.ASC, "createdDate")));
//		List<Object[]> list = resp.getContent();
//		List<AssignmentAndTaskSubmission> assignmentTaskStatusList = new ArrayList<>();
//		for (Object[] row : list) {
//			AssignmentAndTaskSubmission res = new AssignmentAndTaskSubmission();
//			res.setTaskId((long) row[0]);
//			res.setTotalSubmitted((long) row[1]);
//			res.setUnReveiwed((long) row[2]);
//			res.setReveiwed((long) row[3]);
//			res.setTaskTitle((String) row[4]);
//			assignmentTaskStatusList.add(res);
//
//		}

		response.put("data", resp);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> updateTaskQuestion(Long questionId, String question, String videoUrl,
			List<String> questionImages, List<MultipartFile> newImages) {
		Map<String, Object> response = new HashMap<>();
		TaskQuestion taskQuestion = taskQuestionRepository.findByQuestionId(questionId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.NO_DATA_FOUND));

		taskQuestion.setQuestion(question);
		taskQuestion.setVideoUrl(videoUrl);
		if (Objects.isNull(questionImages)) {
			taskQuestion.setQuestionImages(new ArrayList<String>());
		} else {
			taskQuestion.setQuestionImages(questionImages);
		}
		if (Objects.nonNull(newImages) && newImages.size() > 0) {
			List<String> fileNames = newImages.parallelStream()
					.map(file -> fileService.uploadFileInFolder(file, AppConstants.PROFILE_PIC))
					.collect(Collectors.toList());
			taskQuestion.getQuestionImages().addAll(fileNames);
		}
		TaskQuestion save = taskQuestionRepository.save(taskQuestion);
		response.put(AppConstants.MESSAGE, AppConstants.UPDATE_SUCCESSFULLY);
		response.put("question", save);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> deleteAttachement(Long taskId) {
		Task task = taskRepo.findById(taskId).get();
		task.setTaskAttachment("");
		taskRepo.save(task);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> activateTask(Long id) {
		Task task = taskRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException());
		if (task.getIsActive())
			task.setIsActive(false);
		else
			task.setIsActive(true);

		Map<String, Object> res = new HashMap<>();
		res.put(AppConstants.STATUS, taskRepo.save(task).getIsActive());
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAllTaskSubmissionBYTaskId(Long taskId) {
	   List<AssignmentSubmissionResponse>res= taskRepo.getAllTaskSubmissionBYTaskId(taskId);
		return new ResponseEntity<>(res ,HttpStatus.OK);	
	}

}
