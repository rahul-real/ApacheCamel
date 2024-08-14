package com.scheduler.batch.job.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "rahul.sub")
public class SubConfig {
	
	private String username;

	private String password;

	private String subBrokerUrl;

	private String subComponentName;

	private int jmsPoolMaxConnection;

	private int jmsPoolMaxActive;


}
