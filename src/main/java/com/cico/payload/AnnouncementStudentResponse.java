package com.cico.payload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementStudentResponse {

	private Long announcementId;
	private String title;
	private String message;
	private LocalDateTime date;
}
