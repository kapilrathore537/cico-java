package com.cico.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attendance")
public class Attendance {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer attendanceId;
	private Integer studentId;
	private LocalDate checkInDate;
	private LocalTime checkInTime;
	private String checkInLat;
	private String checkInLong;
	private String checkInImage;
	private LocalDate checkOutDate;
	private LocalTime checkOutTime;
	private String checkOutLat;
	private String checkOutLong;
	private String checkOutImage;
	private String checkOutStatus;
	private Long workingHour;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime createdDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime updatedDate;
	private Boolean isMispunch = false;
}
