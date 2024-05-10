package com.cico.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class ChapterResponse {

	private String chapterName;
	private Integer chapterId;
	private String chapterImage;
	private Integer subjectId;
	private String subjectName;
	private Integer scoreGet;

}
