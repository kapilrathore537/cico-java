package com.cico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cico.model.TechnologyStack;
import com.cico.payload.ApiResponse;
import com.cico.service.ITechnologyStackService;

@RestController
@RequestMapping("/technologyStack")
@CrossOrigin("*")
public class TechnologyStackController {

	@Autowired
	private ITechnologyStackService TechnologyStackService;

	// creat TechnologyStack
	@PostMapping("/createTechnologyStackApi")
	public ResponseEntity<ApiResponse> creatTechnologyStack(@RequestParam("technologyName") String technologyName,
			@RequestParam("image") MultipartFile file) {
		TechnologyStack createTechnologyStack = TechnologyStackService.createTechnologyStack(technologyName, file);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(Boolean.TRUE, "TechnologyStack Created Successfully", HttpStatus.CREATED));
	}

	@PutMapping("/updateTechnologyStackApi")
	public ResponseEntity<ApiResponse> uodateTechnologyStack(@RequestParam("technologyStackId") Integer id,
			@RequestParam("technologyName") String technologyName, @RequestParam("image") MultipartFile file) {
		TechnologyStack createTechnologyStack = TechnologyStackService.updateTechnologyStack(id, technologyName, file);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(Boolean.TRUE, "TechnologyStack Created Successfully", HttpStatus.CREATED));
	}

	// get technologyStack
	@GetMapping("/getTechnologyStackApi/{id}")
	public ResponseEntity<TechnologyStack> getTechnologyStack(@PathVariable("id") Integer id) {
		TechnologyStack technologyStack = TechnologyStackService.getTechnologyStack(id);
		return new ResponseEntity<>(technologyStack,HttpStatus.OK);
	}

	// get all technologyStack
	@GetMapping("/getAllTechnologyStackApi")
	public ResponseEntity<List<TechnologyStack>> getAllTechnologyStack() {
		List<TechnologyStack> technologyStack = TechnologyStackService.getAllTechnologyStack();
		return new ResponseEntity<>(technologyStack,HttpStatus.OK);
	}

	@DeleteMapping("deleteTechnologyStackApi/{id}")
	public ResponseEntity<ApiResponse> deleteTechnologyStackApi(@PathVariable("id") Integer id) {
		TechnologyStackService.deleteTechnologyStack(id);
		return new ResponseEntity<ApiResponse>(new ApiResponse(Boolean.TRUE,"Deleted Successfully",HttpStatus.OK),HttpStatus.OK);
	}
}