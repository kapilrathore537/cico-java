package com.cico.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "admin_table")
@Entity
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer adminId;
	private String adminName;
	private String adminEmail;
	private String uuid;
	private String password;
	private String profilePic;
	private String role;

	public Admin(String adminName, String adminEmail, String password) {
		super();
		this.adminName = adminName;
		this.adminEmail = adminEmail;
		
		this.password = password;
	}

}