package com.cico.payload;

import java.time.LocalDate;
import java.time.LocalTime;

import com.cico.model.OrganizationInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class DashboardResponse {
	String attendanceStatus;
	LocalDate checkInDate;
	LocalTime checkInTime;
	String checkInImage;
	String checkOutImage;
	LocalTime checkOutTime;
	LocalDate checkOutDate;
	Boolean isFeesDue;
	LocalDate feesDueDate;
	Boolean isMispunch;
	Boolean isWebLoggedIn = false;
	MispunchResponse mispunchResponseDto;
	StudentResponse studentResponseDto;
	OrganizationInfo organizationInfo;

	OrganizationInfo bhanwarkuaOrganizationInfo;
	OrganizationInfo vijayNagarOrganizatioInfo;
	private Integer seatNumber;
	private Long totalPresent;
	private Long totalAbsent;
	private Long totalMispunch;
	private Long totalEarlyCheckOut;
//	private MockResponse mockResponse;
	// private CounsellingResponse counsellingResponse;
	private boolean mock;
	private boolean counselling;
	private LocalDate mockDate;
	private LocalDate counsellingDate;
	private String counsellingPerson;
	private String mockPerson;

	public DashboardResponse() {
		this.totalAbsent = 0L;
		this.totalEarlyCheckOut = 0L;
		this.totalMispunch = 0L;
		this.totalPresent = 0L;
	}

}
