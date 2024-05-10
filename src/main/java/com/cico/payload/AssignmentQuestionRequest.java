package com.cico.payload;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentQuestionRequest {
	
	private Long assignmentId;

	private MultipartFile taskAttachment;

	private List<TaskQuestionRequest> assignmentQuestion;
	
}
