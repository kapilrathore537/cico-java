package com.cico.payload;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.cico.model.Fees;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeesPayResponse {

	private Integer payId;
	private FeesResponse feesPay;
	private Double feesPayAmount;
	private LocalDate payDate;
	private String recieptNo;
	private String description;
	private LocalDateTime createDate;
	private LocalDateTime updatedDate;
}
