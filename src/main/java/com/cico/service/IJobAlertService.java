package com.cico.service;

import java.util.List;

import com.cico.model.JobAlert;
import com.cico.payload.ApiResponse;
import com.cico.payload.JobAlertResponse;
import com.cico.payload.PageResponse;
import com.cico.util.JobType;

public interface IJobAlertService {

	public JobAlert createJob(Integer technologyStackId,String jobTitle,String jobDescription,String companyName,String experienceRequired,String technicalSkills,JobType type,String jobPackage);

	public JobAlert getJob(Integer jobId);	

	public JobAlert update(Integer jobId, String jobTitle, String jobDescription, String companyName,
			String experienceRequired, String technicalSkills,String jobPackage,String type,Integer technologyStackId);

	public void delete(Integer jobId);


	public List<JobAlert> searchJob(String field,String role);

	public JobAlert activeJob(Integer jobId);

	public PageResponse<JobAlertResponse> getAllJobsAndIntership(Integer page, Integer size);
	
	public PageResponse<JobAlertResponse> getAllJobAlert(int page,int size,JobType type);

	public ApiResponse update(JobAlert jobAlert);
}
