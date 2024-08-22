package com.scheduler.batch.job.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "rahul.autostartup.enable")
public class TransitiveConfig {

	private boolean queueEnabled;
	
	private boolean kafkaEnabled;
	
	
}
