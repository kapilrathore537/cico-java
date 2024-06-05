package com.cico.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentSeatingAlloatment {  

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Integer seatNumber;
	private LocalDate seatAllocatedDate;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn
	private Student student;
	
}
