package com.cico.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cico.model.FeesPay;
import com.cico.payload.FeesPayResponse;
import com.cico.payload.FeesResponse;
import com.cico.payload.PageResponse;

public interface IFeesPayService {

	public FeesPay feesPayService(Integer feesId, Double feesPayAmount, String payDate, String recieptNo, String description);

	public PageResponse<FeesResponse> feesPendingList(Integer page, Integer size);

	public ResponseEntity<?> getAllTransectionByStudentId(Integer studentId);
	
	public PageResponse<FeesPayResponse> feesPayList(Integer page, Integer size);

	public FeesPayResponse findByPayId(Integer payId);

	public FeesPay updateFeesPay(FeesPay feesPay);

	public List<FeesPayResponse> searchByNameInFeesPayList(String fullName);

	public List<FeesPayResponse> searchByMonthInFeesPayList(String startDate, String endDate);

}
