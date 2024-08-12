//package com.scheduler.batch.job.config;
//
//import javax.sql.DataSource;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
//import org.springframework.batch.item.database.JdbcBatchItemWriter;
//import org.springframework.batch.item.database.JdbcCursorItemReader;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.core.SingleColumnRowMapper;
//
//@Configuration
//@EnableBatchProcessing
//public class SpringBatchConfig {
//
//	  @Bean
//	  public JobBuilderFactory jobBuilderFactory() {
//	    return new JobBuilderFactory();
//	  }
//
//	@Autowired
//	private StepBuilderFactory stepBuilderFactory;
//
//	@Autowired
//	public Job job;
//
//	@Autowired
//	private DataSource dataSource;
//	
//	  @Bean
//	  public Job job() throws Exception {
//	    return jobBuilderFactory.get("myJob")
//	        .start(step())
//	        .build();
//	  }
//	  
//	  @Bean
//	  public Step step() throws Exception {
//	    return stepBuilderFactory.get("myStep")
//	        .<Integer, Integer>chunk(10)
//	        .reader(reader())
//	        .processor(processor())
//	        .writer(writer())
//	        .build();
//	  }
//	  
//	  @Bean
//	  public JdbcCursorItemReader<Integer> reader() {
//	    JdbcCursorItemReader<Integer> reader = new JdbcCursorItemReader<>();
//	    reader.setDataSource(dataSource);
//	    reader.setSql("EXEC my_stored_procedure");
//	    reader.setRowMapper(new SingleColumnRowMapper());
//	    return reader;
//	  }
//	  
//	  @Bean
//	  public ItemProcessor<Integer, Integer> processor() {
//	    return new ItemProcessor<Integer, Integer>() {
//	      @Override
//	      public Integer process(Integer item) throws Exception {
//	        // Process the item
//	        return item;
//	      }
//	    };
//	  }
//	  
//	  @Bean
//	  public JdbcBatchItemWriter<Integer> writer() {
//	    JdbcBatchItemWriter<Integer> writer = new JdbcBatchItemWriter<>();
//	    writer.setDataSource(dataSource);
//	    writer.setSql("INSERT INTO my_table (value) VALUES (:value)");
//	    writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
//	    return writer;
//	  }
//	}
