package com.scheduler.batch.job.repo;

import java.util.List;

import org.springframework.stereotype.Component;

import com.common.artifact.RegistrationData;
import com.scheduler.batch.job.dto.Employee;

@Component
public interface JobSchedularRepository {
	
	/**
	 * @param applicationTransactionNumber
	 * @param parId
	 * @return
	 */
	List<RegistrationData> getRegistrationData(String applicationTransactionNumber, String parId);

	void addEmployee(String appTxnNum, List<Employee> employeesDetails);

	List<Object[]> getEmployeeData();

}
