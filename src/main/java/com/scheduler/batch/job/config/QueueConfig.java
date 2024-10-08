package com.scheduler.batch.job.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "rahul.queue")
public class QueueConfig {

	private String username;

	private String password;

	private String brokerUrl;

	private String amqComponentName;

	private String txnAmqComponentName;

	private int jmsPoolMaxConnection;

	private int jmsPoolMaxActive;

}
