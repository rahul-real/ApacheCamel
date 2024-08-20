package com.scheduler.batch.job.scheduler;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.scheduler.batch.job.dto.Employee;
import com.scheduler.batch.job.repo.EmployeeRepo;
import com.scheduler.batch.job.service.JobService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@EnableScheduling
public class JobScheduler {
	
	
	@Autowired
	JobService jobService;
	
	@Autowired
	EmployeeRepo employeeRepo;
	
	@Scheduled(cron = "0 * * * * *", zone = "Asia/Kolkata")
	public void employeeData() {
		
		log.info("Started Job ");
		
		List<Employee> employees = jobService.getEmployeeData();
		
		log.info("Employee Data {} ",employees);
		
		try {

			for(Employee employee : employees) {
				
				employeeRepo.save(employee);
				
			}
			
		} catch (Exception e) {
			log.error("ApplicationTransactionNumber {} has failed while inserting data into h2 {}",UUID.randomUUID().toString(),e.getMessage());
		}
		
		log.info("Job Completed");
		
	}
	
	

}
