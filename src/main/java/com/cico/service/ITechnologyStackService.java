package com.cico.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cico.model.TechnologyStack;

public interface ITechnologyStackService {

	// create
		TechnologyStack createTechnologyStack(String technologyName, MultipartFile file);

		// update
		TechnologyStack updateTechnologyStack(Integer id, String technologyName, MultipartFile file);

		// delete
		void deleteTechnologyStack(Integer id);

		// get TechnologyStack
		TechnologyStack getTechnologyStack(Integer id);

		// get all TechnologyStack
		List<TechnologyStack> getAllTechnologyStack();

		// get technologyStack by name
		TechnologyStack getTechnologyStackByTechnologyName(String name);

}
