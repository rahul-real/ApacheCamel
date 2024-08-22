package com.scheduler.batch.job.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
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
	
    @Value("${spring.datasource.h2.url}")
    private String h2Url;

    @Value("${spring.datasource.h2.username}")
    private String h2Username;

    @Value("${spring.datasource.h2.password}")
    private String h2Password;	

    @Bean("primaryDataSource")
    HikariDataSource primaryDataSource(){
		
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(driverClassName);
		hikariConfig.setJdbcUrl(url);
		hikariConfig.setUsername(username);
		hikariConfig.setPassword(password);
		hikariConfig.setMaximumPoolSize(maxpoolSize);
		hikariConfig.setMinimumIdle(minIdle);
		hikariConfig.setConnectionTimeout(connectionTimeout);
		hikariConfig.setPoolName(poolName + "_primary");
		hikariConfig.setIdleTimeout(idleTimeout);
		
		hikariConfig.addDataSourceProperty("statementPoolingCacheSize", prepStmtCacheSize);
		hikariConfig.addDataSourceProperty("disableStatementPooling", cachePrepStmt);
		hikariConfig.addDataSourceProperty("useUnicode", "false");
		return new HikariDataSource(hikariConfig);
		
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
		hikariConfig.setPoolName(poolName + "_secondary");
		hikariConfig.setIdleTimeout(idleTimeout);
		
		hikariConfig.addDataSourceProperty("statementPoolingCacheSize", prepStmtCacheSize);
		hikariConfig.addDataSourceProperty("disableStatementPooling", cachePrepStmt);
		hikariConfig.addDataSourceProperty("useUnicode", "false");
		return new HikariDataSource(hikariConfig);
		
	}

    @Primary
    @Bean("h2DataSource")
    HikariDataSource h2DataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.h2.Driver");
        hikariConfig.setJdbcUrl(h2Url);
        hikariConfig.setUsername(h2Username);
        hikariConfig.setPassword(h2Password);
        hikariConfig.setMaximumPoolSize(maxpoolSize);
        hikariConfig.setMinimumIdle(minIdle);
        hikariConfig.setConnectionTimeout(connectionTimeout);
        hikariConfig.setPoolName("H2CP");
        hikariConfig.setIdleTimeout(idleTimeout);

        return new HikariDataSource(hikariConfig);
    }

    @Bean(name = "writeEntityManagerFactory")
    EntityManagerFactory writeEntityManagerFactory(@Autowired @Qualifier("primaryDataSource") HikariDataSource primaryDataSource) {
    	LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    	localContainerEntityManagerFactoryBean.setPersistenceUnitName("persistence.writing");
    	localContainerEntityManagerFactoryBean.setDataSource(primaryDataSource);
    	localContainerEntityManagerFactoryBean.setPackagesToScan("com.scheduler.batch.job");
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
    	localContainerEntityManagerFactoryBean.setPackagesToScan("com.scheduler.batch.job");
    	localContainerEntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    	Map<String,Object> jpa = localContainerEntityManagerFactoryBean.getJpaPropertyMap();
    	jpa.put("hibernate.proc.param_null_passing", true);
    	localContainerEntityManagerFactoryBean.afterPropertiesSet();
		return localContainerEntityManagerFactoryBean.getObject();
    	
    }
    
    @Primary
    @Bean(name = "entityManagerFactory")
    EntityManagerFactory h2EntityManagerFactory(@Autowired @Qualifier("h2DataSource") HikariDataSource h2DataSource) {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setPersistenceUnitName("persistence.h2");
        localContainerEntityManagerFactoryBean.setDataSource(h2DataSource);
        localContainerEntityManagerFactoryBean.setPackagesToScan("com.scheduler.batch.job.dto");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.H2);
        vendorAdapter.setGenerateDdl(true);
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        Map<String, Object> jpa = localContainerEntityManagerFactoryBean.getJpaPropertyMap();
        jpa.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        jpa.put("hibernate.hbm2ddl.auto", "create-drop");
        jpa.put("hibernate.show_sql", "true");
        localContainerEntityManagerFactoryBean.afterPropertiesSet();
        return localContainerEntityManagerFactoryBean.getObject();
    }
    
}