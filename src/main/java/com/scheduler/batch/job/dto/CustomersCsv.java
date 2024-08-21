package com.scheduler.batch.job.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "CustomersCsv")
public class CustomersCsv {

	@Id
	private int id;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String gender;
	
	private String phoneNumber;
	
	private String country;
	
	private String dateOfBirth;
	
}
