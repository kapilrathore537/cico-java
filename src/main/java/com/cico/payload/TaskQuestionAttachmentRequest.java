package com.cico.payload;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cico.model.AssignmentTaskQuestion;
import com.cico.model.Task;

import lombok.Data;

@Data
public class TaskQuestionAttachmentRequest {
	
	private MultipartFile taskAttachment;
	private List<AssignmentTaskQuestion> question;
	private Task task;
}
