package com.cico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cico.model.Task;
import com.cico.payload.ApiResponse;
import com.cico.payload.TaskRequest;
import com.cico.service.ITaskService;
import com.cico.util.SubmissionStatus;

@RequestMapping("/task")
@RestController
@CrossOrigin("*")
public class TaskController {

	@Autowired
	ITaskService taskService;

	@PostMapping("/createTask")
	private ResponseEntity<?> createTask(@RequestBody TaskRequest taskRequest) {
		return taskService.createTask(taskRequest);
	}

	@GetMapping("/getTaskById")
	public ResponseEntity<?> getTaskById(@RequestParam("taskId") Long taskId) {
		return this.taskService.getTaskById(taskId);

	}

	@GetMapping("/getAllTaskOfStudent")
	public ResponseEntity<?> getAllTaskOfStudent(@RequestParam("studentId") Integer studentId,@RequestParam("pageSize") Integer pageSise, @RequestParam("pageNumber") Integer pageNumber) {
		return taskService.getAllTaskOfStudent(studentId,pageNumber,pageSise);

	}

	@PostMapping("/studentTaskSubmittion")
	public ResponseEntity<?> StudentTaskSubmittion(@RequestParam("taskId") Long taskId,
			@RequestParam("studentId") Integer studentId,
			@RequestParam(name = "submittionFileName", required = false) MultipartFile file,
			@RequestParam("taskDescription") String taskDescription) throws Exception {
		return taskService.studentTaskSubmittion(taskId, studentId, file, taskDescription);

	}

	@PostMapping("/addQuestionInTask")
	public ResponseEntity<?> addQuestionInTask(@RequestParam("taskId") Long taskId,
			@RequestParam("question") String question,
			@RequestParam(value = "videoUrl", required = false) String videoUrl,
			@RequestParam(value = "questionImages", required = false) List<MultipartFile> questionImages) {
		return taskService.addQuestionInTask(question, videoUrl, questionImages, taskId);

	}

	@PostMapping("/addTaskAttachment")
	public ResponseEntity<?> addTaskAttachment(@RequestParam("taskId") Long taskId,
			@RequestParam(value = "attachment", required = false) MultipartFile attachment) {
		return this.taskService.addTaskAttachment(taskId, attachment);
	}

	@DeleteMapping("/deleteTaskQuestion")
	public ResponseEntity<?> deleteTaskQuestions(@RequestParam("questionId") Long questionId) {
		return taskService.deleteTaskQuestion(questionId);
	}

	@GetMapping("/getSubmitedTaskForStudent")
	public ResponseEntity<?> getSubmitedTaskForStudent(@RequestParam("studentId") Integer studentId ,
			@RequestParam("pageSize") Integer pageSise,
			@RequestParam("pageNumber") Integer pageNumber,
			@RequestParam(value = "status", defaultValue = "NOT_CHECKED_WITH_IT")SubmissionStatus status) {
		return taskService.getSubmitedTaskForStudent(studentId,pageNumber,pageSise,status);
	}

	@GetMapping("/getAllSubmitedTask")
	public ResponseEntity<?> getAllSubmitedTasks(@RequestParam("courseId") Integer courseId,
			@RequestParam("subjectId") Integer subjectId, @RequestParam("status") SubmissionStatus status,@RequestParam("pageSize") Integer pageSise, @RequestParam("pageNumber") Integer pageNumber) {
		return taskService.getAllSubmitedTasks(courseId, subjectId, status,pageNumber,pageSise);
	}

	@PutMapping("/updateSubmitedAssignmentStatus")
	public ResponseEntity<?> updateSubmitedAssignmentStatus(@RequestParam("submissionId") Long submissionId,
			@RequestParam("status") String status, @RequestParam("review") String review) {
		return taskService.updateSubmitedTaskStatus(submissionId, status, review);
	}

	@GetMapping("/getOverAllTaskStatusforBarChart")
	public ResponseEntity<?> getOverAllTaskStatusforBarChart() {
		return taskService.getOverAllTaskStatusforBarChart();
	}

	@GetMapping("/isTaskSubmitted")
	public ResponseEntity<?> isTaskSubmitted(@RequestParam("taskId") Long taskId,
			@RequestParam("studentId") Integer studentId) {
		return taskService.isTaskSubmitted(taskId, studentId);
	}

	@GetMapping("/getSubmissionTaskById")
	public ResponseEntity<?> getSubmissionTaskById(@RequestParam("id") Long id) {
		return taskService.getSubmissionTaskById(id);
	}

	@GetMapping("/getTaskQuestion")
	public ResponseEntity<?> getTaskQuestion(@RequestParam("questionId") long questionId) {
		return taskService.getTaskQuestion(questionId);
	}

	@GetMapping("/getAllSubmissionTaskStatusByCourseIdAndSubjectId")
	public ResponseEntity<?> getAllSubmissionTaskStatusByCourseIdAndSubjectId(
			@RequestParam("courseId") Integer courseId, @RequestParam("subjectId") Integer subjectId,
			@RequestParam("pageSize") Integer pageSise, @RequestParam("pageNumber") Integer pageNumber) {
		return taskService.getAllSubmissionTaskStatusByCourseIdAndSubjectId(courseId, subjectId, pageNumber, pageSise);
	}

	@PutMapping("/updateTaskQuestion")
	public ResponseEntity<?> updateTaskQuestion(@RequestParam("questionId") Long questionId,
			@RequestParam("question") String question, @RequestParam("videoUrl") String videoUrl,
			@RequestParam(value = "questionImages", required = false) List<String> questionImages,
			@RequestParam(value = "newImages", required = false) List<MultipartFile> newImages,
			@RequestParam("taskId") Long taskId) {
		return taskService.updateTaskQuestion(questionId, question, videoUrl, questionImages, newImages,taskId);

	}
	
	@DeleteMapping("/deleteAttachement")
	public ResponseEntity<?> deleteAttachement(@RequestParam("taskId") Long taskId) {
		return taskService.deleteAttachement(taskId);
	}
	
	@PutMapping("/activateTask")
	public ResponseEntity<?>activateTask(@RequestParam("id")Long id){
		return taskService.activateTask(id);
	}
  
	@GetMapping("/getAllTaskSubmissionBYTaskId")
	public ResponseEntity<?>getAllTaskSubmissionBYTaskId(@RequestParam("taskId")Long taskId){
		 return taskService.getAllTaskSubmissionBYTaskId(taskId);
	}
	
}

























