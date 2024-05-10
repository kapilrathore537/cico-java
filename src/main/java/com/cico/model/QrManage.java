package com.cico.model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrManage {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long qrId;
	private String userId;
	private String uuid;
	private String os;
	private String deviceType;
	private String browser;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime loginAt;
	
	
	
	public QrManage(String userId, String uuid) {
		super();
		this.userId = userId;
		this.uuid = uuid;
		this.loginAt = LocalDateTime.now();
	}

	
}
