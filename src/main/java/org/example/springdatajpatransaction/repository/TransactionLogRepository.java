package org.example.springdatajpatransaction.repository;

import org.example.springdatajpatransaction.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {}

