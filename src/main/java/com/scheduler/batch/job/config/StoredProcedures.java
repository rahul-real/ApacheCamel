/**
 * 
 */
package com.scheduler.batch.job.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.scheduler.batch.job.utils.Constants;

import lombok.Data;

/**
 * @author rahul
   @since  10-Jan-2024 2024 8:05:53 pm
 */
@Data
@Component
@ConfigurationProperties(prefix = Constants.STORED_PREFIX)
public class StoredProcedures {
	
	private String getRegistrationData;
	

}
