package com.cico.payload;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(value = Include.NON_NULL)
@NoArgsConstructor
public class TaskQuestionResponse {

	private Long questionId;

	private String question;

	private List<String> questionImages = new ArrayList<>();;

	private String videoUrl;

	public TaskQuestionResponse(Long questionId, String question, List<String> questionImages, String videoUrl) {
		super();
		this.questionId = questionId;
		this.question = question;
		this.questionImages = questionImages;
		this.videoUrl = videoUrl;
	}

	
	
}
