package com.cico.payload;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.cico.model.AssignmentSubmission;
import com.cico.model.AssignmentTaskQuestion;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class AssignmentTaskFilterReponse {

	private Long questionId;
	private String question;
	private String videoUrl;
	private List<String> questionImages;
	private LocalDateTime submissionDate;
	private Long taskNumber;

	@SuppressWarnings("unchecked")
	public AssignmentTaskFilterReponse(Long questionId, String question, String videoUrl, Object questionImages) {
		super();
		this.questionId = questionId;
		this.question = question;
		this.videoUrl = videoUrl;
		this.questionImages = (List<String>) questionImages;
		
	}

	public AssignmentTaskFilterReponse(AssignmentTaskQuestion question, Integer studentId,Long taskNumber) {
		super();
		this.questionId = question.getQuestionId();
		Optional<AssignmentSubmission> findFirst = question.getAssignmentSubmissions().parallelStream()
				.filter(obj -> obj.getStudent().getStudentId() == studentId).findFirst();
		// this.submissionDate = submissionDate;
		if (findFirst.isPresent()) {
			this.submissionDate = findFirst.get().getSubmissionDate();
		}
		this.taskNumber = taskNumber;
	}

}
