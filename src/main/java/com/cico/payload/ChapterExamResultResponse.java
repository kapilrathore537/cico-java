package com.cico.payload;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value =  Include.NON_NULL)
public class ChapterExamResultResponse {

	private Integer id;
	private Integer correcteQuestions;
	private Integer wrongQuestions;
	private Integer notSelectedQuestions;
	private Integer scoreGet;
	public Integer totalQuestion;
	private Map<Integer, String> review = new HashMap<>();
}
