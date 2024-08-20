package com.scheduler.batch.job.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scheduler.batch.job.dto.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long> {

}
