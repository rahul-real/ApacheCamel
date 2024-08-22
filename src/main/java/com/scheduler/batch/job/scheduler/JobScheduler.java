package com.scheduler.batch.job.scheduler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.scheduler.batch.job.dto.Employee;
import com.scheduler.batch.job.repo.EmployeeRepo;
import com.scheduler.batch.job.service.JobService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@EnableScheduling
@AllArgsConstructor
public class JobScheduler {
	
	private final JobLauncher jobLauncher;
	
	private final Job job;
	
	private final JobService jobService;
	
	private final EmployeeRepo employeeRepo;
	
	@Scheduled(cron = "0 0 12 * * *", zone = "Asia/Kolkata")
	public void importCsvToDb() {
		JobParameters jobParameters = new JobParametersBuilder()
				.addLocalDateTime("startAt", LocalDateTime.now()).toJobParameters();
		try {
			jobLauncher.run(job, jobParameters);
		} catch (Exception e) {
			log.info("ApplicationTransactionNumber {} Job failed to run due to {}",UUID.randomUUID().toString(),e.getMessage());
		}
	}
	
	@Scheduled(cron = "0 0 12 * * *", zone = "Asia/Kolkata")
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
