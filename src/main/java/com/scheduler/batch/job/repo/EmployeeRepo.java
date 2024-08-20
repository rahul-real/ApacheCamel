package com.scheduler.batch.job.repo;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scheduler.batch.job.dto.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, BigInteger> {

}
