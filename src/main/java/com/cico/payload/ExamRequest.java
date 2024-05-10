package com.cico.payload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ExamRequest {
	private Integer chapterId;
	private Integer studentId;
	private Integer subjectId;
	private Integer examId;
	private List<Integer> questionList;
	private Map<Integer, String> review = new HashMap<>();

}
