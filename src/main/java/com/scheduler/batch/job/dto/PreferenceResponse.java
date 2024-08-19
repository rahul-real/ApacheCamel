package com.scheduler.batch.job.dto;

import java.util.List;

import lombok.Data;

@Data
public class PreferenceResponse {
	
	private String responseDate;
	
	private List<RegistrationData> registrationData;
	
	
}
