package com.cico.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "feespay")
public class FeesPay {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer payId;
	@ManyToOne
	private Fees fees;
	private Double feesPayAmount;
	private LocalDate payDate;
	private String recieptNo;
	private String description;
	private LocalDateTime createDate;
	private LocalDateTime updatedDate;
	
	public FeesPay(Double feesPayAmount, LocalDate payDate, String recieptNo, String description) {
		super();
		this.feesPayAmount = feesPayAmount;
		this.payDate = payDate;
		this.recieptNo = recieptNo;
		this.description = description;
		this.createDate=LocalDateTime.now();
		this.updatedDate=LocalDateTime.now();
	}
	
	
}
