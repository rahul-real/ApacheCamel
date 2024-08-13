package com.scheduler.batch.job.repo;

import java.util.List;

import org.springframework.stereotype.Component;

import com.common.artifacts.dto.RegistrationData;

@Component
public interface JobSchedularRepository {
	
	/**
	 * @param applicationTransactionNumber
	 * @param parId
	 * @return
	 */
	List<RegistrationData> getRegistrationData(String applicationTransactionNumber, String parId);

}
