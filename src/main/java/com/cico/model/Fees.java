package com.cico.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fees")
public class Fees {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer feesId;
	@ManyToOne
	private Student student;
	@OneToOne
	@JoinColumn
	private Course course;
	private Double finalFees;
	private Double feesPaid;
	private Double remainingFees;
	public Fees(Student student, Course course, Double finalFees, LocalDate date) {
		super();
		this.student = student;
		this.course = course;
		this.finalFees = finalFees;
		this.date = date;
	}
	private LocalDate date;
	private LocalDate createdDate;
	private LocalDate updatedDate;
	private Boolean isCompleted=false;
}
