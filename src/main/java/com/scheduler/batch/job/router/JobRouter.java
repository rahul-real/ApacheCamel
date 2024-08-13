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
		.log("In FirstQueue received a message of Body ${body}")
		.setHeader("Consumer1", constant("true"))
		.to(topics.getFirstTopic())
		.end();
		
		
		from(queues.getConsumer1())
		.log("In Consumer.MASH_001.VirtualTopic.Eidiko received a message of Body ${body}")
		.end();
		
		from(queues.getConsumer2())
		.log("In Consumer.MASH_002.VirtualTopic.Eidiko received a message of Body ${body}")
		.end();
		
		from(queues.getConsumer3())
		.log("In Consumer.MASH_003.VirtualTopic.Eidiko received a message of Body ${body}")
		.end();
		
		from(queues.getConsumer4())
		.log("In Consumer.MASH_004.VirtualTopic.Eidiko received a message of Body ${body}")
		.end();
		
		from("kafka:first-kafka-topic")
		.log("In kafka:first-kafka-topic received a messagae of Body ${body}")
		.to(queues.getFirstQueue())
		.end();
	}

}
