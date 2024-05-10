package com.cico.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class ChapterContentResponse {

	private Integer id;
	private String title;
	private String subTitle;
	private String content;
	private Boolean isDeleted = false;
	private String  chapterName;
}
