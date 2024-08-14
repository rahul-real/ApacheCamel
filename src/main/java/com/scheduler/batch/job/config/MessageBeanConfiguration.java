/**
 * 
 */
package com.scheduler.batch.job.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.jms.JMSException;

/**
 * @author rahul
 * @since 08-Aug-2024 2024 11:06:33 am
 */
@Configuration
public class MessageBeanConfiguration {

	@Autowired
	private CamelContext camelContext;

	@Autowired
	private QueueConfig queueConfig;
	
	@Autowired
	private SubConfig pubConfig;

	@PostConstruct
	public void addCamelContext() throws NumberFormatException, JMSException {
		
		ActiveMQComponent activeMQComponent = new ActiveMQComponent();
		PooledConnectionFactory pooledConnectionFactory = activeMqConnectionFactory();	
		activeMQComponent.setConnectionFactory(pooledConnectionFactory);
		camelContext.addComponent(queueConfig.getAmqComponentName(), activeMQComponent);
		
		ActiveMQComponent pubActiveMQComponent = new ActiveMQComponent();
		JmsPoolConnectionFactory pubPooledConnectionFactory = pubConnectionFactory();
		pubActiveMQComponent.setConnectionFactory(pubPooledConnectionFactory);
		camelContext.addComponent(pubConfig.getSubComponentName(), pubActiveMQComponent);		

	}

	private JmsPoolConnectionFactory pubConnectionFactory() {
		
		JmsConnectionFactory jmsConnectionFactory = new JmsConnectionFactory(pubConfig.getUsername(), 
				pubConfig.getPassword(), pubConfig.getSubBrokerUrl());
		JmsPoolConnectionFactory jmsPoolConnectionFactory = new JmsPoolConnectionFactory();
		jmsPoolConnectionFactory.setConnectionFactory(jmsConnectionFactory);
		jmsPoolConnectionFactory.setMaxConnections(pubConfig.getJmsPoolMaxConnection());
		jmsPoolConnectionFactory.setMaxSessionsPerConnection(pubConfig.getJmsPoolMaxActive());
		return jmsPoolConnectionFactory;
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
