package com.scheduler.batch.job.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "rahul.autostartup")
public class TransitiveConfig {

	private boolean enabled;
	
}
