package com.scheduler.batch.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
	        title = "JobApplication APIs",
	        version = "1.0",
	        description = "API documentation for the JobApplication"
	    )
	)
@EnableJpaRepositories(basePackages = "com.scheduler.batch.job.repo")
@EntityScan(basePackages = "com.scheduler.batch.job.dto")
public class JobApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobApplication.class, args);
	}

}
