package com.scheduler.batch.job.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.artifacts.dto.PreferenceRequest;
import com.common.artifacts.dto.PreferenceResponse;
import com.common.artifacts.dto.RegistrationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.batch.job.repo.JobSchedularRepository;

@Service
public class JobService {
	
	@Autowired
	ProducerTemplate producerTemplate;
	
	@Autowired
	JobSchedularRepository jobSchedularRepository;
	
	@Autowired
	ObjectMapper mapper;

	public PreferenceResponse getRegistrationData(String appTxnNum, PreferenceRequest request) throws Exception {
		
		PreferenceResponse response = new PreferenceResponse();
		
		String parId = String.join(",", request.getParid());
		
		List<RegistrationData> registrationData = jobSchedularRepository.getRegistrationData(appTxnNum, parId);
		
		response.setRegistrationData(registrationData);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		LocalDateTime date = LocalDateTime.now();
		
		response.setResponseDate(date.format(formatter));
		
		String json = mapper.writeValueAsString(response);
		
		producerTemplate.sendBody("kafka:first-kafka-topic", json);
		
		return response;
	}
	
	

}
