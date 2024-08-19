package com.scheduler.batch.job.dto;

import lombok.Data;

@Data
public class ValidationError {

	private String field;
	
	private String message;
	
	public ValidationError(String field,String message) {
		this.field = field;
		this.message = message;
	}
}
