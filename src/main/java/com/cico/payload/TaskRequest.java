package com.cico.payload;

import com.cico.model.Course;
import com.cico.model.Subject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

	private String taskName;

//	private MultipartFile taskAttachment;
//
//	private List<TaskQuestion> question;

	private Course course;
	private Subject subject;
	private String attachmentStatus;

}
