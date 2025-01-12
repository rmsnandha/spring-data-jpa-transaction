package org.example.springdatajpatransaction.repository;

import org.example.springdatajpatransaction.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {}

