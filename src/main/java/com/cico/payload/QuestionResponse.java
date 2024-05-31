package com.cico.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class QuestionResponse {
	private Integer questionId;
	private String questionContent;
	private String option1;
	private String option2;
	private String option3;
	private String option4;
	private String correctOption;
	private String selectedOption;// select by student 
	private String questionImage;
	private Boolean isSelected;
}
