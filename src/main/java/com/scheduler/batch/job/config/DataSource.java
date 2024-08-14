package com.scheduler.batch.job.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;

/**
 * @author rahul
   @since  09-Jan-2024 2024 1:07:58 pm
 */
@Configuration
@EnableTransactionManagement
public class DataSource {
	
	@Value("${spring.datasource.username}")
	private String username;
	
	@Value("${spring.datasource.password}")
	private String password;
	
	@Value("${spring.datasource.url}")
	private String url;
	
	@Value("${spring.datasource.url2}")
	private String url2;
	
	@Value("${spring.datasource.driverClassName}")
	private String driverClassName;
	
	@Value("${spring.datasource.hikari.pool-name}")
	private String poolName;
	
	@Value("${spring.datasource.hikari.connection-timeout}")
	private int connectionTimeout;
	
	@Value("${spring.datasource.hikari.max-lifeTime}")
	private int maxLifeTime;
	
	@Value("${spring.datasource.hikari.maximum-pool-size}")
	private int maxpoolSize;
	
	@Value("${spring.datasource.hikari.idle-timeout}")
	private int idleTimeout;
	
	@Value("${spring.datasource.disable-prepared-statements-pool}")
	private String cachePrepStmt;
	
	@Value("${spring.datasource.prepared-statements-cache-size}")
	private String prepStmtCacheSize;
	
	@Value("${spring.datasource.hikari.minimum-idle}")
	private int minIdle;

    @Bean("primaryDataSource")
    @Primary
    HikariDataSource primaryDataSource(){
		
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(driverClassName);
		hikariConfig.setJdbcUrl(url);
		hikariConfig.setUsername(username);
		hikariConfig.setPassword(password);
		hikariConfig.setMaximumPoolSize(maxpoolSize);
		hikariConfig.setMinimumIdle(minIdle);
		hikariConfig.setConnectionTimeout(connectionTimeout);
		hikariConfig.setPoolName(poolName);
		hikariConfig.setIdleTimeout(idleTimeout);
		
		hikariConfig.addDataSourceProperty("statementPoolingCacheSize", prepStmtCacheSize);
		hikariConfig.addDataSourceProperty("disableStatementPooling", cachePrepStmt);
		hikariConfig.addDataSourceProperty("useUnicode", "false");
		HikariDataSource hds = new HikariDataSource(hikariConfig);
		return hds;
		
	}
    
    @Bean("secondaryDataSource")
    HikariDataSource secondaryDataSource(){
		
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(driverClassName);
		hikariConfig.setJdbcUrl(url2);
		hikariConfig.setUsername(username);
		hikariConfig.setPassword(password);
		hikariConfig.setMaximumPoolSize(maxpoolSize);
		hikariConfig.setMinimumIdle(minIdle);
		hikariConfig.setConnectionTimeout(connectionTimeout);
		hikariConfig.setPoolName(poolName);
		hikariConfig.setIdleTimeout(idleTimeout);
		
		hikariConfig.addDataSourceProperty("statementPoolingCacheSize", prepStmtCacheSize);
		hikariConfig.addDataSourceProperty("disableStatementPooling", cachePrepStmt);
		hikariConfig.addDataSourceProperty("useUnicode", "false");
		HikariDataSource hds = new HikariDataSource(hikariConfig);
		return hds;
		
	}


    @Primary
    @Bean(name = "entityManagerFactory")
    EntityManagerFactory writeEntityManagerFactory(@Autowired @Qualifier("primaryDataSource") HikariDataSource primaryDataSource) {
    	LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    	localContainerEntityManagerFactoryBean.setPersistenceUnitName("persistence.writing");
    	localContainerEntityManagerFactoryBean.setDataSource(primaryDataSource);
    	localContainerEntityManagerFactoryBean.setPackagesToScan("com.rahul.learn");
    	localContainerEntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    	Map<String,Object> jpa = localContainerEntityManagerFactoryBean.getJpaPropertyMap();
    	jpa.put("hibernate.proc.param_null_passing", true);
    	localContainerEntityManagerFactoryBean.afterPropertiesSet();
		return localContainerEntityManagerFactoryBean.getObject();
    	
    }
    
    @Bean(name = "secondaryEntityManagerFactory")
    EntityManagerFactory secondaryEntityManagerFactory(@Autowired @Qualifier("secondaryDataSource") HikariDataSource secondaryDataSource) {
    	LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    	localContainerEntityManagerFactoryBean.setPersistenceUnitName("persistence.writing");
    	localContainerEntityManagerFactoryBean.setDataSource(secondaryDataSource);
    	localContainerEntityManagerFactoryBean.setPackagesToScan("com.rahul.learn");
    	localContainerEntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    	Map<String,Object> jpa = localContainerEntityManagerFactoryBean.getJpaPropertyMap();
    	jpa.put("hibernate.proc.param_null_passing", true);
    	localContainerEntityManagerFactoryBean.afterPropertiesSet();
		return localContainerEntityManagerFactoryBean.getObject();
    	
    }
    
    
    
}