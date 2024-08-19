package com.scheduler.batch.job.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.scheduler.batch.job.dto.Employee;
import com.scheduler.batch.job.service.JobService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@EnableScheduling
public class JobScheduler {
	
	
	@Autowired
	JobService jobService;
	
	
	@Scheduled(cron = "0 35 11 * * *", zone = "Asia/Kolkata")
	public void employeeData() {
		
		log.info("Started Job ");
		
		List<Employee> employee = jobService.getEmployeeData();
		
		log.info("Employee Data {} ",employee);
		
		log.info("Job Completed");
		
	}

}
