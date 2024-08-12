/**
 * 
 */
package com.scheduler.batch.job.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.jms.JMSException;

/**
 * @author rahul
 * @since 09-Jan-2024 2024 1:06:33 pm
 */
@Configuration
public class MessageBeanConfiguration {

	@Autowired
	private CamelContext camelContext;

	@Autowired
	private QueueConfig queueConfig;

	@PostConstruct
	public void addCamelContext() throws NumberFormatException, JMSException {
		ActiveMQComponent activeMQComponent = new ActiveMQComponent();
		PooledConnectionFactory pooledConnectionFactory = activeMqConnectionFactory();	
		activeMQComponent.setConnectionFactory(pooledConnectionFactory);
		camelContext.addComponent(queueConfig.getAmqComponentName(), activeMQComponent);

	}

	private PooledConnectionFactory activeMqConnectionFactory() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(queueConfig.getUsername(),queueConfig.getPassword()
				,queueConfig.getBrokerUrl());
		connectionFactory.setUseAsyncSend(false);
		PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
		pooledConnectionFactory.setConnectionFactory(connectionFactory);
		pooledConnectionFactory.setMaxConnections(queueConfig.getJmsPoolMaxConnection());
		pooledConnectionFactory.setMaximumActiveSessionPerConnection(queueConfig.getJmsPoolMaxActive());
		return pooledConnectionFactory;
	}

}
