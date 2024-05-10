package com.cico.payload;

import java.time.LocalDateTime;
import java.util.List;

import com.cico.util.DiscussionFormEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
public class DiscussionFormResponse {

	private Integer id;
	private DiscussionFormEnum type;
	public LocalDateTime createdDate;
	private String content;
	private Integer studentId;
	private String studentName;
	private String studentProfilePic;
	private String file;
	private String courseName;
	public List<LikeResponse> likes;
	private List<CommentResponse> comments;
	private Boolean isLike;
	private Boolean isCommented;
	private String audioFile;

	@Override
	public String toString() {
		return "{\"id\":" + id + ", \"type\":\"" + type + "\", \"createdDate\":\"" + createdDate + "\", \"content\":\""
				+ content + "\", \"studentId\":\"" + studentId + "\", \"studentName\":\"" + studentName
				+ "\", \"studentProfilePic\":\"" + studentProfilePic + "\", \"file\":\"" + file
				+ "\", \"courseName\":\"" + courseName + "\", \"likes\":" + likes + ", \"comments\":" + comments
				+ ", \"isLike\":" + isLike + ", \"isCommented\":" + isCommented + ", \"audioFile\":\"" + audioFile
				+ "\"}";
	}

}
