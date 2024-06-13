package com.cico.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cico.exception.ResourceNotFoundException;
import com.cico.kafkaServices.KafkaProducerService;
import com.cico.model.JobAlert;
import com.cico.model.TechnologyStack;
import com.cico.payload.ApiResponse;
import com.cico.payload.JobAlertResponse;
import com.cico.payload.NotificationInfo;
import com.cico.payload.PageResponse;
import com.cico.repository.JobAlertRepository;
import com.cico.repository.StudentRepository;
import com.cico.service.IJobAlertService;
import com.cico.service.ITechnologyStackService;
import com.cico.util.AppConstants;
import com.cico.util.JobType;
import com.cico.util.NotificationConstant;

@Service
public class JobAlertServiceImpl implements IJobAlertService {

	@Autowired
	private JobAlertRepository repository;

	@Autowired
	private ITechnologyStackService technologyStackService;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private KafkaProducerService kafkaProducerService;

	@Autowired
	private StudentRepository studentRepository;

	@Override
	public JobAlert createJob(Integer technologyStackId, String jobTitle, String jobDescription, String companyName,
			String experienceRequired, String technicalSkills, JobType type, String jobPackage) {
		JobAlert alert = new JobAlert(jobTitle, jobDescription, companyName, experienceRequired, technicalSkills, true,
				type, jobPackage);
		alert.setIsDeleted(false);
		alert.setCreatedDate(LocalDateTime.now());
		alert.setUpdatedDate(LocalDateTime.now());
		alert.setTechnologyStack(technologyStackService.getTechnologyStack(technologyStackId));

		// .....firebase notification .....//

		List<NotificationInfo> fcmIds = studentRepository.fetchAllStudentIsCompletedFalse();

		String message = String.format(
				type.equals(JobType.JOB) ? "An exciting job opportunity has just been posted. Explore the details!."
						: "An exciting internship opportunity has just been posted. Explore the details!");

		List<NotificationInfo> newlist = fcmIds.stream().parallel().map(obj1 -> {
			obj1.setTitle(type.equals(JobType.JOB) ? "New Job Alert!" : " New Internship Alert!");
			obj1.setMessage(message);
			return obj1;
		}).toList();

		kafkaProducerService.sendNotification(NotificationConstant.ASSIGNMENT_TOPIC, newlist.toString());
		// .....firebase notification .....//

		return repository.save(alert);
	}

	@Override
	public JobAlert getJob(Integer jobId) {
		// TODO Auto-generated method stub
		Optional<JobAlert> findById = repository.findById(jobId);
		if (!findById.isPresent()) {
			throw new ResourceNotFoundException("Job is not found from given Id");
		}

		return findById.get();
	}

	@Override
	public JobAlert update(Integer jobId, String jobTitle, String jobDescription, String companyName,
			String experienceRequired, String technicalSkills, String jobPackage, String type,
			Integer technologyStackId) {
		JobAlert alert = new JobAlert();
		Optional<JobAlert> findById = repository.findById(jobId);
		alert = findById.get();

		if (jobTitle != null)
			alert.setJobTitle(jobTitle);

		if (jobDescription != null)
			alert.setJobDescription(jobDescription);

		if (companyName != null)
			alert.setCompanyName(companyName);

		if (experienceRequired != null)
			alert.setExperienceRequired(experienceRequired);

		if (technicalSkills != null)
			alert.setTechnicalSkills(technicalSkills);

		if (technologyStackId != null) {
			TechnologyStack technologyStack = technologyStackService.getTechnologyStack(technologyStackId);
			alert.setTechnologyStack(technologyStack);
		} else {
			alert.setTechnologyStack(alert.getTechnologyStack());
		}
		alert.setUpdatedDate(LocalDateTime.now());
		return repository.save(alert);
	}

	@Override
	public void delete(Integer jobId) {

		Optional<JobAlert> findById = repository.findById(jobId);
		if (!findById.isPresent()) {
			throw new ResourceNotFoundException("Job Not Found From Given Id");
		}
		findById.get().setIsDeleted(true);
		repository.save(findById.get());

	}

	@Override
	public List<JobAlert> searchJob(String field, String role) {
		return role.equalsIgnoreCase("student") == true ? repository.jobSearchStudent(field)
				: repository.jobSearchAdmin(field);
	}

	@Override
	public JobAlert activeJob(Integer jobId) {
		JobAlert jobAlert = repository.findById(jobId)
				.orElseThrow(() -> new ResourceNotFoundException("Job not found !!"));

		jobAlert.setIsActive(!jobAlert.getIsActive());

		return repository.save(jobAlert);
	}

	@Override
	public PageResponse<JobAlertResponse> getAllJobsAndIntership(Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "jobId");
		Page<JobAlert> jobAlert = repository.findAllByIsDeleted(false, pageable);
		if (jobAlert.getNumberOfElements() == 0) {
			return new PageResponse<>(Collections.emptyList(), jobAlert.getNumber(), jobAlert.getSize(),
					jobAlert.getTotalElements(), jobAlert.getTotalPages(), jobAlert.isLast());
		}
		List<JobAlertResponse> alertResponses = Arrays
				.asList(mapper.map(jobAlert.getContent(), JobAlertResponse[].class));

		return new PageResponse<>(alertResponses, jobAlert.getNumber(), jobAlert.getSize(), jobAlert.getTotalElements(),
				jobAlert.getTotalPages(), jobAlert.isLast());
	}

	@Override
	public PageResponse<JobAlertResponse> getAllJobAlert(int page, int size, JobType type) {

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "type");
		Page<JobAlert> jobAlert = repository.findAllByTypeAndIsDeletedAndIsActive(type, false, true, pageable);
		if (jobAlert.getNumberOfElements() == 0) {
			return new PageResponse<>(Collections.emptyList(), jobAlert.getNumber(), jobAlert.getSize(),
					jobAlert.getTotalElements(), jobAlert.getTotalPages(), jobAlert.isLast());
		}
		List<JobAlertResponse> alertResponses = Arrays
				.asList(mapper.map(jobAlert.getContent(), JobAlertResponse[].class));

		return new PageResponse<>(alertResponses, jobAlert.getNumber(), jobAlert.getSize(), jobAlert.getTotalElements(),
				jobAlert.getTotalPages(), jobAlert.isLast());
	}

	@Override
	public ApiResponse update(JobAlert jobAlert) {
	
		JobAlert save = repository.save(jobAlert);
		if (Objects.nonNull(save))
			return new ApiResponse(Boolean.TRUE, AppConstants.CREATE_SUCCESS, HttpStatus.CREATED);
		return new ApiResponse(Boolean.FALSE, AppConstants.FAILED, HttpStatus.OK);

	}

}