package com.cico.payload;

import lombok.Data;

@Data
public class AttendenceOfMonth {
	private Long totalPresent=0l;
	private Long totalAbsent=0l;
	private Long totalMispunch=0l;
	private Long totalEarlyCheckOut=0l;
	private Long totalLeaves = 0l;

}
