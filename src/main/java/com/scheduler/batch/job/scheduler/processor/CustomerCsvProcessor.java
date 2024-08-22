package com.scheduler.batch.job.scheduler.processor;

import org.springframework.batch.item.ItemProcessor;

import com.scheduler.batch.job.dto.CustomersCsv;

public class CustomerCsvProcessor implements ItemProcessor<CustomersCsv,CustomersCsv>{

	@Override
	public CustomersCsv process(CustomersCsv csv) throws Exception {
		return csv;
	}
}
