package com.scheduler.batch.job.dto;

import java.math.BigInteger;

import lombok.Data;

@Data
public class Employee {
	
	private BigInteger employeeId;
	
	private String employeeName;

	private String employeeAddress;
	
	private String jobTitle;
	
	private BigInteger phoneNumber;
	
	private String workLocation;
	
	private String department;

}
