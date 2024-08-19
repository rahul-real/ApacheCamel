package com.scheduler.batch.job.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PreferenceRequest {
	
	
	private List<String> parid;
	
	private String requestDate;
	
	@NotBlank(message =  "RequestComments should not be blank")
	private String requestComments;

}
