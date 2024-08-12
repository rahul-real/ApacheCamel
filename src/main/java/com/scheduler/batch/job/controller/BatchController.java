package com.scheduler.batch.job.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/job")
@Slf4j
public class BatchController {
	
	@GetMapping("/ping/{name}")
	public String ping(@PathVariable String name) {
		log.info("name"+ name);
		return name;
	}

}
