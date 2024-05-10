package com.cico.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponse {

	private Integer adminId;
	private String adminName;
	private String adminEmail;
	private String uuid;
	private String profilePic;
}
