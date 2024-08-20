package com.scheduler.batch.job.router;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.scheduler.batch.job.config.Queues;
import com.scheduler.batch.job.config.Topics;
import com.scheduler.batch.job.config.TransitiveConfig;

@Component
public class JobRouter extends RouteBuilder {
	
	@Autowired
	Queues queues;
	
	@Autowired
	Topics topics;
	
	@Autowired
	TransitiveConfig transitiveConfig;

	@Override
	public void configure() throws Exception {
		
		from(queues.getFirstQueue()).autoStartup(transitiveConfig.isEnabled())
		.log("In FirstQueue received a message of Body ${body}")
		.setHeader("Consumer1", constant("true"))
		.to(topics.getFirstTopic())
		.end();
		
		
		from(queues.getConsumer1()).autoStartup(transitiveConfig.isEnabled())
		.log("In Consumer.MASH_001.VirtualTopic.Eidiko received a message of Body ${body}")
		.to(queues.getSubQueue())
		.end();
		
		from(queues.getConsumer2()).autoStartup(transitiveConfig.isEnabled())
		.log("In Consumer.MASH_002.VirtualTopic.Eidiko received a message of Body ${body}")
		.end();
		
		from(queues.getConsumer3()).autoStartup(transitiveConfig.isEnabled())
		.log("In Consumer.MASH_003.VirtualTopic.Eidiko received a message of Body ${body}")
		.end();
		
		from(queues.getConsumer4()).autoStartup(transitiveConfig.isEnabled())
		.log("In Consumer.MASH_004.VirtualTopic.Eidiko received a message of Body ${body}")
		.end();
		
		from("kafka:first-kafka-topic").autoStartup(transitiveConfig.isEnabled())
		.log("In kafka:first-kafka-topic received a messagae of Body ${body}")
		.to(queues.getFirstQueue())
		.end();
		
		from(queues.getSubQueue()).autoStartup(transitiveConfig.isEnabled())
		.log("In FirstAmqpQueue received a messagae of Body ${body}")
		.end();
		
	}

}
