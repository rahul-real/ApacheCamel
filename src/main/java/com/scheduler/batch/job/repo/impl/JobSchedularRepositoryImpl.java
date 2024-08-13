package com.scheduler.batch.job.repo.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.artifacts.dto.RegistrationData;
import com.scheduler.batch.job.config.StoredProcedures;
import com.scheduler.batch.job.repo.JobSchedularRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@Transactional
public class JobSchedularRepositoryImpl implements JobSchedularRepository {
	
	@Autowired
	EntityManagerFactory entityManagerFactory;
	
	@Autowired
	StoredProcedures storedProcedures;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RegistrationData> getRegistrationData(String applicationTransactionNumber, String parId) {
		EntityManager entityManager = null;
		StoredProcedureQuery storedProcedureQuery = null;
		List<RegistrationData> registrationData = null;
		try {
			entityManager = entityManagerFactory.createEntityManager();
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


}
