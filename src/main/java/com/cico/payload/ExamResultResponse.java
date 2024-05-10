package com.cico.payload;

import com.cico.util.ExamType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamResultResponse {

	private Integer id;
	private Integer correcteQuestions;
	private Integer wrongQuestions;
	private Integer notSelectedQuestions;
	private String profilePic;
	private Integer studentId;
	private String studentName;
	private Integer scoreGet;
	public Integer totalQuestion;
//	private int examType;

}
