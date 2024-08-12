package com.scheduler.batch.job.router;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.scheduler.batch.job.config.Queues;
import com.scheduler.batch.job.config.Topics;

@Component
public class JobRouter extends RouteBuilder {
	
	@Autowired
	Queues queues;
	
	@Autowired
	Topics topics;

	@Override
	public void configure() throws Exception {
		
		from(queues.getFirstQueue())
		.log("Body ${body}")
		.setHeader("Consumer1", constant("true"))
		.to(topics.getFirstTopic())
		.end();
		
		
		from(queues.getReadTopic())
		.log("Read Message ${body}")
		.end();
		
		from(queues.getConsumer1())
		.log("Read Message ${body}")
		.end();
		
		from(queues.getConsumer2())
		.log("Read Message ${body}")
		.end();
		
		from(queues.getConsumer3())
		.log("Read Message ${body}")
		.end();
		
		from("kafka:first-kafka-topic")
		.log("Body ${body}")
		.to(queues.getFirstQueue())
		.end();
	}

}
