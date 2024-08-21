package com.scheduler.batch.job.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.common.artifact.PreferenceRequest;
import com.common.artifact.PreferenceResponse;
import com.scheduler.batch.job.dto.Employee;
import com.scheduler.batch.job.logtime.annotation.LogExecutionTime;
import com.scheduler.batch.job.scheduler.JobScheduler;
import com.scheduler.batch.job.service.JobService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/job")
@Slf4j
public class BatchController {
	
	@Autowired
	JobService jobservice;
	
	@Autowired
	JobScheduler jobScheduler;
	
	@LogExecutionTime
	@GetMapping("/ping/{name}")
	public String ping(@PathVariable String name) {
		log.info("name"+ name);
		return name;
	}
	
	@LogExecutionTime
    @Operation(summary = "Preference Register Data", description = "Endpoint to get preference Registration data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registered preference data"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
	@PostMapping("/registration/data")
	public Mono<PreferenceResponse> registrationData(@Valid @RequestBody PreferenceRequest request) throws Exception{
		
		String appTxnNum = UUID.randomUUID().toString();
		
		PreferenceResponse preferenceResponse = jobservice.getRegistrationData(appTxnNum,request);
		
		return Mono.just(preferenceResponse);
	}
	
	@LogExecutionTime
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
	
	@LogExecutionTime
	@PostMapping("/add/employee")
	public Flux<?> addEmployeeDetails(@RequestBody List<Employee> employeesDetails) {
		
		String appTxnNum = UUID.randomUUID().toString();
		
		log.info("ApplicationTransactionNumber {} API /add/employee got called with Request: {} ",appTxnNum,employeesDetails);
		
		jobservice.addEmployeeDetails(appTxnNum,employeesDetails);
		
		return Flux.fromIterable(employeesDetails);
	}
	
	@LogExecutionTime
	@GetMapping("/send/mail")
	public String sendMail() {
		jobservice.sendMail("rahul.vodala@gmail.com", "Hello", "This is a test email");
	    return "Email sent successfully";
	 }	
	
	@LogExecutionTime
    @GetMapping("/read/latestEmail")
    public String readLatestEmail() {
    	jobservice.readLatestEmail();
        return "Latest email read successfully!";
    }	
    
	@LogExecutionTime
    @GetMapping("/read-latest-email")
    public String readLatestEmailC() {
        String host = "imap.example.com";
        String user = "your-email@example.com";
        String password = "your-password";
        
        return jobservice.readLatestEmail(host, user, password);
    }
    
	@LogExecutionTime
    @PostMapping("/add/customer")
    public void addCustomerH2(@RequestParam long id,@RequestParam String name) {
    	
    	jobservice.addCustomer(id,name);
    	
    }
	
	@LogExecutionTime
    @PostMapping("/add/customer/account")
    public void addCustomerAccountH2(@RequestParam long id,@RequestParam String name) {
    	
    	jobservice.addAccount(id,name);
    	
    }
	
    
	@LogExecutionTime
    @DeleteMapping("/remove/customer/{id}")
    public void purgeCustomer(@PathVariable long id) {
    	
    	jobservice.purgeCustomer(id);
    	
    }
	
	@LogExecutionTime
	@GetMapping("/employeeData/h2")
	public void addEmployeeDatafromSqlServerToH2() {
		
		jobScheduler.employeeData();
		
	}
	
	@LogExecutionTime
	@GetMapping("/csv")
	public void csvToDbJob() {
		
		jobScheduler.importCsvToDb();
		
	}

}	
