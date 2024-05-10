package com.cico.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeResponse {

	private Integer id;
	public String createdDate;
	private String studentName;
	private String studentProfilePic;
	private boolean isLike;
}
