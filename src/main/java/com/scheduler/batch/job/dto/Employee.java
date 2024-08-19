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
	
	public Employee(Object[] object) {
		this.employeeId = BigInteger.valueOf((Long) object[0]);
		this.employeeName = String.valueOf(object[1]);
		this.employeeAddress = String.valueOf(object[2]);
		this.jobTitle = String.valueOf(object[3]);
		this.phoneNumber = BigInteger.valueOf((Long) object[4]);
		this.workLocation = String.valueOf(object[5]);
		this.department = String.valueOf(object[6]);
	}

}
