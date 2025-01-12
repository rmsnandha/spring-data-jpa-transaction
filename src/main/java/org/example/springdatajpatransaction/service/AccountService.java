package org.example.springdatajpatransaction.service;

import org.example.springdatajpatransaction.entity.Account;
import org.example.springdatajpatransaction.entity.TransactionLog;
import org.example.springdatajpatransaction.repository.AccountRepository;
import org.example.springdatajpatransaction.repository.TransactionLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;

    public AccountService(AccountRepository accountRepository, TransactionLogRepository transactionLogRepository) {
        this.accountRepository = accountRepository;
        this.transactionLogRepository = transactionLogRepository;
    }

    // Propagation.REQUIRED
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void requiredTransaction(Long id, Double amount) {
        Account account = accountRepository.findById(id).orElseThrow();
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        logTransaction(account.getId(), amount, "REQUIRED");
    }

    // Propagation.REQUIRES_NEW
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public void requiresNewTransaction(Long id, Double amount) {
        Account account = accountRepository.findById(id).orElseThrow();
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        logTransaction(account.getId(), amount, "REQUIRES_NEW");
    }

    // Propagation.NESTED
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.REPEATABLE_READ)
    public void nestedTransaction(Long id, Double amount) {
        Account account = accountRepository.findById(id).orElseThrow();
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        logTransaction(account.getId(), amount, "NESTED");
    }

    // Propagation.MANDATORY
    @Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.READ_COMMITTED)
    public void mandatoryTransaction(Long id, Double amount) {
        Account account = accountRepository.findById(id).orElseThrow();
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        logTransaction(account.getId(), amount, "MANDATORY");
    }

    // Propagation.NOT_SUPPORTED
    @Transactional(propagation = Propagation.NOT_SUPPORTED, isolation = Isolation.READ_UNCOMMITTED)
    public void notSupportedTransaction(Long id, Double amount) {
        Account account = accountRepository.findById(id).orElseThrow();
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        logTransaction(account.getId(), amount, "NOT_SUPPORTED");
    }

    // Propagation.NEVER
    @Transactional(propagation = Propagation.NEVER, isolation = Isolation.DEFAULT)
    public void neverTransaction(Long id, Double amount) {
        Account account = accountRepository.findById(id).orElseThrow();
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        logTransaction(account.getId(), amount, "NEVER");
    }

    // Propagation.SUPPORTS
    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED)
    public void supportsTransaction(Long id, Double amount) {
        Account account = accountRepository.findById(id).orElseThrow();
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        logTransaction(account.getId(), amount, "SUPPORTS");
    }

    private void logTransaction(Long accountId, Double amount, String propagationType) {
        TransactionLog log = new TransactionLog();
        log.setAccountId(accountId);
        log.setAmount(amount);
        log.setPropagationType(propagationType);
        transactionLogRepository.save(log);
    }

    // Example Nested Method Calls with Multiple Entities
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void performComplexTransaction(Long accountId, Long anotherAccountId, Double amount) {
        requiredTransaction(accountId, amount);

        try {
            requiresNewTransaction(anotherAccountId, -amount);
        } catch (Exception e) {
            // Handle rollback for the requiresNewTransaction without affecting the outer transaction
            System.out.println("Exception in requiresNewTransaction: " + e.getMessage());
        }

        nestedTransaction(accountId, amount / 2);
    }
}

