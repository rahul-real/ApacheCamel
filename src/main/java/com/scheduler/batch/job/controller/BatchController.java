package com.scheduler.batch.job.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.common.artifacts.dto.PreferenceRequest;
import com.common.artifacts.dto.PreferenceResponse;
import com.scheduler.batch.job.service.JobService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/job")
@Slf4j
public class BatchController {
	
	@Autowired
	JobService jobservice;
	
	@GetMapping("/ping/{name}")
	public String ping(@PathVariable String name) {
		log.info("name"+ name);
		return name;
	}
	
	@PostMapping("/registration/data")
	public Mono<PreferenceResponse> registrationData(@Valid @RequestBody PreferenceRequest request) throws Exception{
		
		String appTxnNum = UUID.randomUUID().toString();
		
		PreferenceResponse preferenceResponse = jobservice.getRegistrationData(appTxnNum,request);
		
		return Mono.just(preferenceResponse);
	}
	
	@GetMapping("/webclient")
	public Mono<?> webClient(){
		WebClient webClient = WebClient.builder()
				.baseUrl("http://localhost:8081")
				.build();
		Mono<int[]> respone = webClient.get().uri("/sortBinaryArrayInLinearTime").retrieve().bodyToMono(int[].class);
		respone.subscribe(data ->  {
			log.info("data {}",data);
		});
		return Mono.just(respone);
	}

}
