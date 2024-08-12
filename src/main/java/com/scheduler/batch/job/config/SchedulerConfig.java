//package com.scheduler.batch.job.config;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.JobParametersInvalidException;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
//import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
//import org.springframework.batch.core.repository.JobRestartException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import lombok.extern.slf4j.Slf4j;
//
//@EnableScheduling
//@Configuration
//@Slf4j
//public class SchedulerConfig {
//
//	@Autowired
//	public JobLauncher jobLauncher;
//
//	@Autowired
//	public Job job;
//
//		@Scheduled(cron = "0 0 12 * * *")
//		public void runJob() throws JobExecutionAlreadyRunningException, JobRestartException,
//				JobInstanceAlreadyCompleteException, JobParametersInvalidException {
//	        JobParameters jobParameters = new JobParametersBuilder()
//	                .addLong("time", System.currentTimeMillis())
//	                .toJobParameters();
//			JobExecution execution = jobLauncher.run(job, jobParameters);
//			log.info("Job completed with status: {}", execution.getStatus());
//		}
//
//}
