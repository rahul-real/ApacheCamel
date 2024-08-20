package com.scheduler.batch.job.repo.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.common.artifact.RegistrationData;
import com.scheduler.batch.job.config.StoredProcedures;
import com.scheduler.batch.job.dto.Employee;
import com.scheduler.batch.job.logtime.annotation.LogExecutionTime;
import com.scheduler.batch.job.repo.JobSchedularRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@Transactional
public class JobSchedularRepositoryImpl implements JobSchedularRepository {
	
	
	@Qualifier("writeEntityManagerFactory")
	@Autowired
	EntityManagerFactory writeEntityManagerFactory;
	
	@Qualifier("secondaryEntityManagerFactory")
	@Autowired
	EntityManagerFactory secondaryEntityManagerFactory;
	
	@Autowired
	StoredProcedures storedProcedures;
	
	@SuppressWarnings("unchecked")
	@Override
	@LogExecutionTime
	public List<RegistrationData> getRegistrationData(String applicationTransactionNumber, String parId) {
		EntityManager entityManager = null;
		StoredProcedureQuery storedProcedureQuery = null;
		List<RegistrationData> registrationData = null;
		try {
			entityManager = writeEntityManagerFactory.createEntityManager();
			storedProcedureQuery = entityManager.createStoredProcedureQuery(storedProcedures.getGetRegistrationData());
			storedProcedureQuery.registerStoredProcedureParameter("ParId", String.class, ParameterMode.IN)
			.setParameter("ParId", parId);
			storedProcedureQuery.execute();
			List<Object[]> object = storedProcedureQuery.getResultList();
			if(object != null && !object.isEmpty()) {
				registrationData = object.stream().map(RegistrationData::new).collect(Collectors.toList());
				
			}
		}catch (Exception e) {
			log.info("ApplicationSystemNumber {} Failed due to {}",e.getMessage());
			throw e;
		}finally {
			closeResources(entityManager);
		}
		return registrationData;
	}

	private void closeResources(EntityManager entityManager) {
		if(null!=entityManager) {
			entityManager.close();
		}
	}

	@Override
	@LogExecutionTime
	public void addEmployee(String appTxnNum, List<Employee> employeesDetails) {
		EntityManager entityManager = null;
		StoredProcedureQuery storedProcedureQuery = null;
		try {
			entityManager = secondaryEntityManagerFactory.createEntityManager();
			for(Employee employee: employeesDetails) {
				storedProcedureQuery = entityManager.createStoredProcedureQuery(storedProcedures.getAddEmployeeDetails());
				storedProcedureQuery.registerStoredProcedureParameter("employeeId", BigInteger.class, ParameterMode.IN)
									.registerStoredProcedureParameter("employeeName", String.class, ParameterMode.IN)
									.registerStoredProcedureParameter("employeeAddress", String.class, ParameterMode.IN)
									.registerStoredProcedureParameter("jobTitle", String.class, ParameterMode.IN)
									.registerStoredProcedureParameter("phoneNumber", BigInteger.class, ParameterMode.IN)
									.registerStoredProcedureParameter("workLocation", String.class, ParameterMode.IN)
									.registerStoredProcedureParameter("department", String.class, ParameterMode.IN)
									.setParameter("employeeId", employee.getEmployeeId())
									.setParameter("employeeName", employee.getEmployeeName())
									.setParameter("employeeAddress", employee.getEmployeeAddress())
									.setParameter("jobTitle", employee.getJobTitle())
									.setParameter("phoneNumber", employee.getPhoneNumber())
									.setParameter("workLocation", employee.getWorkLocation())
									.setParameter("department", employee.getDepartment());
				storedProcedureQuery.execute();
			}
			
		} catch (Exception e) {
			log.info("ApplicationTransactionNumber {} Exception while adding Employee Details {} ", appTxnNum,e.getMessage());
			throw e;
		}finally {
			closeResources(entityManager);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@LogExecutionTime
	public List<Object[]> getEmployeeData() {
		List<Object[]> respone = null;
		EntityManager entityManager = null;
		StoredProcedureQuery storedProcedureQuery = null;
		try {
			entityManager = secondaryEntityManagerFactory.createEntityManager();
			storedProcedureQuery = entityManager.createStoredProcedureQuery(storedProcedures.getEmployeeDetails());
			storedProcedureQuery.execute();
			respone = storedProcedureQuery.getResultList();
		}catch (Exception e) {
			log.info("ApplicationTransactionNumber {} Exception while adding Employee Details {} ",UUID.randomUUID().toString(),e.getMessage());
			throw e;
		}finally {
			closeResources(entityManager);
		}
		return respone;
	}


}
