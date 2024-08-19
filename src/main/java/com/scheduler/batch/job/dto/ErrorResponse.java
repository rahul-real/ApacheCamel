package com.scheduler.batch.job.dto;

import java.util.List;

import lombok.Data;

@Data
public class ErrorResponse {
	
	private String message;
	
	private int statusCode;
	
	private List<ValidationError> errors;
	
	public ErrorResponse(String message, int code, List<ValidationError> validationErrors) {
		
		this.message = message;
		this.statusCode = code;
		this.errors = validationErrors;
		
	}

}
