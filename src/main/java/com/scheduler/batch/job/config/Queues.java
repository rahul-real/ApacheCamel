package com.scheduler.batch.job.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * @author rahul
   @since  09-Jan-2024 2024 1:09:39 pm
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "rahul.external.queues")
public class Queues {
	
	private String firstQueue;
	
	private String consumer1;
	
	private String consumer2;
	
	private String consumer3;
	
	private String consumer4;
	
	private String subQueue;
}
