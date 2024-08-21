package com.scheduler.batch.job.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scheduler.batch.job.dto.CustomersCsv;

public interface CustomerCsvRepo extends JpaRepository<CustomersCsv, Integer> {

}
