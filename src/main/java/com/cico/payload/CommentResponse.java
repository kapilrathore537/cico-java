package com.cico.payload;

import java.time.LocalDateTime;
import java.util.List;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CommentResponse {

	private Integer id;
	public LocalDateTime createdDate;
	private String content;
	private String studentName;
	private String studentProfilePic;
	private Integer studentId;
	private String file;
	
    private List<CommentReplyResponse>commentReplyResponses;

}
