package com.cico.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cico.exception.ResourceNotFoundException;
import com.cico.model.TechnologyStack;
import com.cico.repository.TechnologyStackRepository;
import com.cico.service.IFileService;
import com.cico.service.ITechnologyStackService;
import com.cico.util.AppConstants;

@Service
public class TechnologyStackServiceImpl implements ITechnologyStackService {

	@Autowired
	private TechnologyStackRepository technologyStackRepository;
	@Autowired
	private IFileService fileService;

	@Override
	public TechnologyStack createTechnologyStack(String technologyName, MultipartFile file) {

		String fileName = fileService.uploadFileInFolder(file, AppConstants.TECHNOLOGY_IMAGES);
		System.out.println(technologyName);
		TechnologyStack technologyStack = new TechnologyStack(technologyName, fileName);
		technologyStack.setCreatedDate(LocalDateTime.now());
		technologyStack.setIsDeleted(false);
		technologyStack.setUpdatedDate(LocalDateTime.now());
		TechnologyStack technologyStack2 = technologyStackRepository.save(technologyStack);
		if (Objects.nonNull(technologyStack2)) {
			return technologyStack;
		}
		return null;
	}

	@Override
	public TechnologyStack updateTechnologyStack(Integer id, String technologyName, MultipartFile file) {
		TechnologyStack technologyStack = technologyStackRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("technologyStack not found with this id"));
		String fileName = "";

		if (technologyName != null)
			technologyStack.setTechnologyName(technologyName);
		else
			technologyStack.setTechnologyName(technologyStack.getTechnologyName());

		if (file != null && !file.isEmpty()) {
			fileName = fileService.uploadFileInFolder(file, AppConstants.TECHNOLOGY_IMAGES);
		}
		technologyStack.setUpdatedDate(LocalDateTime.now());
		return technologyStackRepository.save(technologyStack);

	}

	@Override
	public void deleteTechnologyStack(Integer id) {
		TechnologyStack technologyStack = technologyStackRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("technologyStack not found with this id"));
		technologyStack.setIsDeleted(true);
		technologyStackRepository.save(technologyStack);

	}

	@Override
	public TechnologyStack getTechnologyStack(Integer id) {
		return technologyStackRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("technologyStack not found with this id"));
	}

	@Override
	public List<TechnologyStack> getAllTechnologyStack() {
		return technologyStackRepository.findAll();
	}

	@Override
	public TechnologyStack getTechnologyStackByTechnologyName(String name) {
		return technologyStackRepository.findByTechnologyName(name);
	}

}
