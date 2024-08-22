/**
 * 
 */
package com.scheduler.batch.job.config;

import java.util.Arrays;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

/**
 * @author rahul
 * @since 08-Aug-2024 2024 11:06:33 am
 */
@Configuration
@AllArgsConstructor
public class MessageBeanConfiguration {

	private final CamelContext camelContext;
	private final QueueConfig queueConfig;
	private final SubConfig subConfig;

	@PostConstruct
	public void addCamelContext() throws NumberFormatException {
		
		ActiveMQComponent activeMQComponent = new ActiveMQComponent();
		PooledConnectionFactory pooledConnectionFactory = activeMqConnectionFactory();	
		activeMQComponent.setConnectionFactory(pooledConnectionFactory);
		camelContext.addComponent(queueConfig.getAmqComponentName(), activeMQComponent);
		
		ActiveMQComponent subActiveMQComponent = new ActiveMQComponent();
		JmsPoolConnectionFactory subPooledConnectionFactory = subConnectionFactory();
		subActiveMQComponent.setConnectionFactory(subPooledConnectionFactory);
		camelContext.addComponent(subConfig.getSubComponentName(), subActiveMQComponent);		

	}

	private JmsPoolConnectionFactory subConnectionFactory() {
		
		JmsConnectionFactory jmsConnectionFactory = new JmsConnectionFactory(subConfig.getUsername(), 
				subConfig.getPassword(), subConfig.getSubBrokerUrl());
		JmsPoolConnectionFactory jmsPoolConnectionFactory = new JmsPoolConnectionFactory();
		jmsPoolConnectionFactory.setConnectionFactory(jmsConnectionFactory);
		jmsPoolConnectionFactory.setMaxConnections(subConfig.getJmsPoolMaxConnection());
		jmsPoolConnectionFactory.setMaxSessionsPerConnection(subConfig.getJmsPoolMaxActive());
		return jmsPoolConnectionFactory;
	}

	private PooledConnectionFactory activeMqConnectionFactory() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(queueConfig.getUsername(),queueConfig.getPassword()
				,queueConfig.getBrokerUrl());
		connectionFactory.setTrustedPackages(Arrays.asList("com.scheduler.batch.job", "com.scheduler.batch.job.router"));
		connectionFactory.setUseAsyncSend(false);
		PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
		pooledConnectionFactory.setConnectionFactory(connectionFactory);
		pooledConnectionFactory.setMaxConnections(queueConfig.getJmsPoolMaxConnection());
		pooledConnectionFactory.setMaximumActiveSessionPerConnection(queueConfig.getJmsPoolMaxActive());
		return pooledConnectionFactory;
	}

}
