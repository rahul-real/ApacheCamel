package com.scheduler.batch.job.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.scheduler.batch.job.dto.CustomersCsv;
import com.scheduler.batch.job.repo.CustomerCsvRepo;
import com.scheduler.batch.job.scheduler.processor.CustomerCsvProcessor;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SpringBatchConfig {
	
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;	
	private final CustomerCsvRepo customerCsvRepo;

	@Bean
	FlatFileItemReader<CustomersCsv> flatFileItemReader(){
		FlatFileItemReader<CustomersCsv> fileItemReader = new FlatFileItemReader<>();
		fileItemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
		fileItemReader.setName("csvReader");
		fileItemReader.setLinesToSkip(1);
		fileItemReader.setLineMapper(lineMapper());
		return fileItemReader;
		
	}
	
	@Bean
	CustomerCsvProcessor csvProcessor(){
		return new CustomerCsvProcessor();
	}
	
	private LineMapper<CustomersCsv> lineMapper() {
		DefaultLineMapper<CustomersCsv> lineMapper = new DefaultLineMapper<>();
		
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames("id","firstName","lastName","email","gender","phoneNumber","country","dateOfBirth");
		
		BeanWrapperFieldSetMapper<CustomersCsv> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
		beanWrapperFieldSetMapper.setTargetType(CustomersCsv.class);
		
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
		
		return lineMapper;
	}
	
	@Bean
	RepositoryItemWriter<CustomersCsv> itemWriter(){
		RepositoryItemWriter<CustomersCsv> repositoryItemWriter = new RepositoryItemWriter<>();
		repositoryItemWriter.setRepository(customerCsvRepo);
		repositoryItemWriter.setMethodName("save");
		return repositoryItemWriter;
	}

    @Bean
    Step step1(ItemReader<CustomersCsv> reader, ItemProcessor<CustomersCsv, CustomersCsv> processor, ItemWriter<CustomersCsv> writer) {
        return new StepBuilder("csv-step", jobRepository)
                .<CustomersCsv, CustomersCsv>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
		taskExecutor.setConcurrencyLimit(10);
		return taskExecutor;
	}

	@Bean
    Job runJob(Step step1) {
        return new JobBuilder("importCustomers", jobRepository)
                .start(step1)
                .build();
    }
}
