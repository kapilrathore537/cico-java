package com.cico.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Fees;
import com.cico.model.Student;
import com.cico.payload.FeesResponse;
import com.cico.payload.PageResponse;
import com.cico.repository.FeesRepository;
import com.cico.repository.StudentRepository;
import com.cico.service.IFeesService;
import com.cico.util.AppConstants;

@Service
public class FeesServiceImpl implements IFeesService {

	@Autowired
	private FeesRepository feesRepository;

	@Autowired
	private StudentRepository studentRepository;
	
	@Override
	public FeesResponse createStudentFees(Integer studentId, Integer courseId, Double finalFees, String date) {
		// TODO Auto-generated method stub
		
		Fees fees=new Fees(null, null, finalFees, LocalDate.parse(date));
		Fees findByStudent = feesRepository.findByStudent(studentRepository.findByStudentId(studentId));
		if(Objects.isNull(findByStudent)) {
		Student student = studentRepository.findById(studentId).get();
		fees.setStudent(student);
		fees.setCourse(student.getCourse());
		fees.setRemainingFees(finalFees);
		fees.setFeesPaid(0.0);
		fees.setCreatedDate(LocalDate.now());
		fees.setUpdatedDate(LocalDate.now());
		  Fees feesData = feesRepository.save(fees);
		 return  setFeesResponse(feesData);
		}
		throw new ResourceNotFoundException(AppConstants.DATA_ALREADY_EXIST);
	}

	@Override
	public PageResponse<FeesResponse> feesList(Integer page, Integer size) {
	    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "feesId");
	    Page<Fees> fees = feesRepository.findAllByIsCompleted(false, pageable);

	    if (fees.getNumberOfElements() == 0) {
	        return new PageResponse<>(Collections.emptyList(), fees.getNumber(), fees.getSize(), fees.getTotalElements(), fees.getTotalPages(), fees.isLast());
	    }

	    List<FeesResponse> feesResponseList = new ArrayList<>();

	    for (Fees fee : fees.getContent()) {
	        FeesResponse feesResponse = setFeesResponse(fee);
	        feesResponseList.add(feesResponse);
	    }

	    return new PageResponse<>(feesResponseList, fees.getNumber(), fees.getSize(), fees.getTotalElements(), fees.getTotalPages(), fees.isLast());
	}


	@Override
	public FeesResponse findByFeesId(Integer feesId) {
		// TODO Auto-generated method stub
		Fees fees = feesRepository.findById(feesId).orElseThrow(()
				->new ResourceNotFoundException("Fees Not found for given Id!!"));
		return setFeesResponse(fees);
	}

	@Override
	public List<FeesResponse> searchByName(String fullName,String status) {
		List<Fees> findByStudent =  null ;
		if(AppConstants.COMPLETED.equals(status)) {
			findByStudent = feesRepository.findByStudentFullNameContaining(fullName,true);
		}else{
			findByStudent = feesRepository.findByStudentFullNameContaining(fullName,false);
		}
           
          if(Objects.isNull(findByStudent)) {
        	  throw new ResourceNotFoundException("Student not found");
          }
         
		return findByStudent.stream().map(obj->setFeesResponse(obj)).collect(Collectors.toList());
	}

	@Override
	public List<FeesResponse> findFeesByDates(String startDate, String endDate,String status) {
        List<Fees> findFeesByGivenDates = null;
        if(AppConstants.COMPLETED.equals(status)) {
        	findFeesByGivenDates = feesRepository.findFeesByGivenDates(LocalDate.parse(startDate),LocalDate.parse(endDate),true);
        }else {
        	findFeesByGivenDates = feesRepository.findFeesByGivenDates(LocalDate.parse(startDate),LocalDate.parse(endDate),false);        	
        }
        if(Objects.isNull(findFeesByGivenDates)) {
      	  throw new ResourceNotFoundException("Fees is not found from given Dates");
        }
       
        return findFeesByGivenDates.stream().map(obj->setFeesResponse(obj)).collect(Collectors.toList());
	}

	@Override
	public PageResponse<FeesResponse> feesCompleteList(Integer page, Integer size) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "feesId");
		Page<Fees> fees = feesRepository.findAllByIsCompleted(true,pageable);
		if(fees.getNumberOfElements()==0) {
			return new PageResponse<>(Collections.emptyList(), fees.getNumber(), fees.getSize(), fees.getTotalElements(), fees.getTotalPages() ,fees.isLast());
		}
		return new PageResponse<>( fees.stream().map(obj->setFeesResponse(obj)).collect(Collectors.toList()), fees.getNumber(), fees.getSize(), fees.getTotalElements(), fees.getTotalPages(), fees.isLast());
	}

	@Override
	public Fees updateFees(Fees fees) {
		Fees feesData = feesRepository.findByFeesId(fees.getFeesId());
		if(Objects.nonNull(feesData)) {
			feesData.setFinalFees(fees.getFinalFees());
			feesData.setRemainingFees(fees.getRemainingFees());
			feesData.setFeesPaid(fees.getFeesPaid());
			Fees save = feesRepository.save(feesData);
			return save;
		}
		return null;
	}

	@Override
	public ResponseEntity<?> getFeesCollectionMonthAndYearWise(int year) {
		 Map<Integer,Double>response = new HashMap<>();
         List<Object[]> totalFeesPaidByMonthAndYear = feesRepository.getTotalFeesPaidByMonth(year);
		 for(Object[] row :totalFeesPaidByMonthAndYear) {
			 response.put((Integer)row[0],(Double)row[1]);
		 }
		 return new ResponseEntity<>(response,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getTotalfeesCollection() {
	 Map<String,Object>feeResponse = new HashMap<>();
	 List< Object[] > row = feesRepository.getTotalFeeCollection();
	     feeResponse.put("Total", row.get(0)[0]); 
	   feeResponse.put("Pending",row.get(0)[1]);
	     feeResponse.put("Collected", row.get(0)[2]);
	    return new ResponseEntity<>(feeResponse,HttpStatus.OK);
	}
	
	
	public  FeesResponse setFeesResponse(Fees fees){
		
		FeesResponse feesResponse=new FeesResponse();
		feesResponse.setCollege(fees.getStudent().getCollege());
		feesResponse.setCourseFees(fees.getCourse().getCourseFees());
		feesResponse.setCourseId(fees.getCourse().getCourseId());
		feesResponse.setCourseName(fees.getCourse().getCourseName());
		feesResponse.setCreatedDate(fees.getCreatedDate());
		feesResponse.setCurrentCourse(fees.getStudent().getCurrentCourse());
		feesResponse.setDate(fees.getDate());
		feesResponse.setDob(fees.getStudent().getDob());
		feesResponse.setEmail(fees.getStudent().getEmail());
		feesResponse.setFeesId(fees.getFeesId());
		feesResponse.setFeesPaid(fees.getFeesPaid());
		feesResponse.setFinalFees(fees.getFinalFees());
		feesResponse.setIsCompleted(fees.getIsCompleted());
		feesResponse.setMobile(fees.getStudent().getMobile());
		feesResponse.setProfilePic(fees.getStudent().getProfilePic());
		feesResponse.setRemainingFees(fees.getRemainingFees());
		feesResponse.setStudentId(fees.getStudent().getStudentId());
		feesResponse.setUpdatedDate(fees.getUpdatedDate());
		feesResponse.setFullName(fees.getStudent().getFullName());
	    
		return feesResponse;
	}
 
	
}
