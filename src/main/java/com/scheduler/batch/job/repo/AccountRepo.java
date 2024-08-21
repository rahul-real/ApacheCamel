package com.scheduler.batch.job.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scheduler.batch.job.dto.Account;

@Repository
public interface AccountRepo extends JpaRepository<Account, Integer> {

}
