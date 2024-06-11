package com.cico.service;

import java.util.List;
import java.util.Map;

import com.cico.model.Batch;
import com.cico.payload.ApiResponse;
import com.cico.payload.BatchRequest;
import com.cico.payload.BatchResponse;


public interface IBatchService {


	ApiResponse deleteBatch(Integer batchId);

	BatchResponse getBatchById(Integer batchId);

	List<BatchResponse> getAllBatches(Integer studentId);

	List<Batch> getUpcomingBatches();
	
	ApiResponse updateBatchStatus(Integer batchId);

	ApiResponse createBatch(BatchRequest request);

	ApiResponse updateBatch(Batch batch);
	
	Batch getFirstUpcomingBatchOfCurrentCourse(String course);

}
