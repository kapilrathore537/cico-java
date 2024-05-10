package com.cico.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cico.model.Fees;
import com.cico.payload.FeesResponse;
import com.cico.payload.PageResponse;

public interface IFeesService {

	

	public FeesResponse createStudentFees(Integer studentId, Integer courseId, Double finalFees, String date);

	public PageResponse<FeesResponse> feesList(Integer page, Integer size);

	public FeesResponse findByFeesId(Integer feesId);

	public List<FeesResponse> searchByName(String fullName,String status);

	public List<FeesResponse> findFeesByDates(String startDate, String endDate,String status);

	public PageResponse<FeesResponse> feesCompleteList(Integer page, Integer size);

	public Fees updateFees(Fees fees);

	ResponseEntity<?> getFeesCollectionMonthAndYearWise(int year);

	public ResponseEntity<?> getTotalfeesCollection();

	

}
