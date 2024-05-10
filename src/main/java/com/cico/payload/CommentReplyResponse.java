package com.cico.payload;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@ToString
public class CommentReplyResponse {
	private Integer id;
	public LocalDateTime createdDate;
	private String content;
	private String studentName;
	private String studentProfilePic;
	private String file;
	private Integer studentId;
//	@Override
//	public String toString() {
////		return "CommentReplyResponse [id=" + id + ", createdDate=" + createdDate + ", content=" + content
////				+ ", studentName=" + studentName + ", studentProfilePic=" + studentProfilePic + ", studentId="
////				+ studentId + ", file=" + file + "]";
//		Gson gson = new Gson();
//		return gson.toJson(this);
//	}
 
	
}
