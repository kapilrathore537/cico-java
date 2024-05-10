package com.cico.payload;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(value = Include.NON_NULL)
public class LikeResponse {

	private String type;

	private Integer id;
	public LocalDateTime createdDate;
	private String studentName;
	private String studentProfilePic;
	private Integer studentId;
	private Integer discussionFormId;
	private boolean isLike;

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

}
