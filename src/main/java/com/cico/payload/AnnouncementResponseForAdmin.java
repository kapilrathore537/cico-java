package com.cico.payload;

import java.time.LocalDateTime;
import java.util.List;

import com.cico.model.MessageSeenBy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementResponseForAdmin {

	private Long announcementId;
	private String title;
	private String message;
	private LocalDateTime date;
	private List<String> courseName;
	private MessageSeenBy seenBy;
}
