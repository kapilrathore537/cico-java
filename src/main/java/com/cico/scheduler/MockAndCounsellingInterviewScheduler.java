package com.cico.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cico.service.IStudentService;

@Component
public class MockAndCounsellingInterviewScheduler {
	@Autowired
	private IStudentService studentService;

	@Scheduled(cron = "0 0 10 * * 1-6") // this scheduler is execute MON-SAT 6AM
	public void selectStudentForMockAndCounselling() {
		studentService.fetchRandomStudentForMockInterview();
		studentService.fetchRandomStudentForCounselling();
		System.out.println("select student for mock And counselling");
	}

	@Scheduled(cron = "0 0 22 * * 1-6") // this scheduler is execute MON-SAT 10PM
	public void checkStudentForMockAndCousellingCompleteOrNot() {
		studentService.checkMockIsCompleteOrNot();
		studentService.checkCounsellingkIsCompleteOrNot();
		System.out.println("check student mock ands couselling is complete or not");
	}

}
