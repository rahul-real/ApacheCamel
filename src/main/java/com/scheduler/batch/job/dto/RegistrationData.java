package com.scheduler.batch.job.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistrationData {
	
	private String accountNumber;
	
	private String parId;
	
	private Byte registrationStatusCode;
	
	public RegistrationData(Object[] objects) {
		
		this.accountNumber = String.valueOf(objects[0]);
		this.parId = String.valueOf(objects[1]);
		this.registrationStatusCode = Byte.valueOf((byte) objects[2]);
		
	}

}
