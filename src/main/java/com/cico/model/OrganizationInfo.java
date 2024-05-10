package com.cico.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "oganization_info")
@Entity
public class OrganizationInfo {
	@Id
	 Integer organizationId;
	 String name;
	 String address;
	 String latitude;
	 String longitude;
	 String inOutRange;
	 String workingHours;
	 String halfDayWorkingHour;
	 LocalDateTime createdDate;
	 String officeLatVj;
	 String officeLongVj;
}
