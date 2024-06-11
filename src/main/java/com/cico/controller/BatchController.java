package com.cico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cico.model.Batch;
import com.cico.payload.ApiResponse;
import com.cico.payload.BatchRequest;
import com.cico.payload.BatchResponse;
import com.cico.service.IBatchService;

@RestController
@RequestMapping("/batch")
@CrossOrigin("*")
public class BatchController {

	@Autowired
	IBatchService batchService;

	@PostMapping("/createBatch")
	public ResponseEntity<?> createBatch(@RequestBody BatchRequest request) {
		System.out.println(request);
		 ApiResponse createBatch = batchService.createBatch(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(createBatch);

	}

	@PutMapping("/updateBatch")
	public ResponseEntity<?> updateBatch(@RequestBody Batch batch) {

		 ApiResponse updateBatch = batchService.updateBatch(batch);
		return ResponseEntity.ok(updateBatch);

	}

	@PutMapping("/deleteBatch/{batchId}")
	public ResponseEntity<ApiResponse> deleteBatch(@PathVariable("batchId") Integer batchId) {
	ApiResponse response = batchService.deleteBatch(batchId);
	return ResponseEntity.ok(response);
	}

	@GetMapping("/getBatchById/{batchId}")
	public ResponseEntity<?> getBatchById(@PathVariable("batchId") Integer batchId) {
		 BatchResponse batchResponse = batchService.getBatchById(batchId);
		return ResponseEntity.ok(batchResponse);

	}

	@GetMapping("/getAllBatches")
	public ResponseEntity<?> getAllBatches(@RequestParam("studentId") Integer studentId) {
		List<BatchResponse> batches = batchService.getAllBatches(studentId);
		return ResponseEntity.ok(batches);

	}
  
	@GetMapping("/getUpcomingBatches")
	public ResponseEntity<List<Batch>> getUpcomingBatches() {
		List<Batch> batches = batchService.getUpcomingBatches();
		return ResponseEntity.ok(batches);

	}

	@PutMapping("/updateBatchStatus/{batchId}")
	public ResponseEntity<ApiResponse> updateBatchStatus(@PathVariable("batchId") Integer batchId) {
		ApiResponse response = batchService.updateBatchStatus(batchId);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(response);

	}
	
	@GetMapping("/firstUpcomingBatchOfCurrentCourse/{courseName}")
	public ResponseEntity<?> firstUpcomingBatchOfCurrentCourse(
			@PathVariable String courseName
			) {
		return new ResponseEntity<>(batchService.getFirstUpcomingBatchOfCurrentCourse(courseName),HttpStatus.OK);
	}
	
	
}
