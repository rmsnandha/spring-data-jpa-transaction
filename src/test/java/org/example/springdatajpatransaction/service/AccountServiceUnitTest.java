package org.example.springdatajpatransaction.service;

import org.example.springdatajpatransaction.entity.Account;
import org.example.springdatajpatransaction.entity.TransactionLog;
import org.example.springdatajpatransaction.repository.AccountRepository;
import org.example.springdatajpatransaction.repository.TransactionLogRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.TransactionSystemException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import(AccountService.class)
public class AccountServiceUnitTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @Autowired
    private AccountService accountService;

    @Test
    @Rollback
    void testRequiredTransaction() {
        Account account = new Account(null, "John Doe", 1000.0);
        account = accountRepository.save(account);

        accountService.requiredTransaction(account.getId(), 500.0);

        Account updatedAccount = accountRepository.findById(account.getId()).orElseThrow();
        assertEquals(1500.0, updatedAccount.getBalance());

        TransactionLog log = transactionLogRepository.findAll().get(0);
        assertEquals(account.getId(), log.getAccountId());
        assertEquals(500.0, log.getAmount());
        assertEquals("REQUIRED", log.getPropagationType());
    }

    @Test
    @Rollback
    void testRequiresNewTransaction() {
        Account account = new Account(null, "John Doe", 1000.0);
        account = accountRepository.save(account);

        accountService.requiresNewTransaction(account.getId(), 300.0);

        Account updatedAccount = accountRepository.findById(account.getId()).orElseThrow();
        assertEquals(1300.0, updatedAccount.getBalance());

        TransactionLog log = transactionLogRepository.findAll().get(0);
        assertEquals(account.getId(), log.getAccountId());
        assertEquals(300.0, log.getAmount());
        assertEquals("REQUIRES_NEW", log.getPropagationType());
    }

    @Test
    @Rollback
    @Disabled
    void testNestedTransaction() {
        Account account = new Account(null, "John Doe", 1000.0);
        account = accountRepository.save(account);

        accountService.nestedTransaction(account.getId(), 200.0);

        Account updatedAccount = accountRepository.findById(account.getId()).orElseThrow();
        assertEquals(1200.0, updatedAccount.getBalance());

        TransactionLog log = transactionLogRepository.findAll().get(0);
        assertEquals(account.getId(), log.getAccountId());
        assertEquals(200.0, log.getAmount());
        assertEquals("NESTED", log.getPropagationType());
    }

    @Test
    @Rollback
    void testPerformComplexTransaction() {
        Account account1 = new Account(null, "John Doe", 1000.0);
        Account account2 = new Account(null, "Jane Smith", 2000.0);

        account1 = accountRepository.save(account1);
        account2 = accountRepository.save(account2);

        accountService.performComplexTransaction(account1.getId(), account2.getId(), 500.0);

        Account updatedAccount1 = accountRepository.findById(account1.getId()).orElseThrow();
        Account updatedAccount2 = accountRepository.findById(account2.getId()).orElseThrow();

        assertEquals(1750.0, updatedAccount1.getBalance());
        assertEquals(1500.0, updatedAccount2.getBalance());

        assertEquals(3, transactionLogRepository.count());
    }

    @Test
    void testMandatoryTransactionWithoutExistingTransaction() {

        final Account account = accountRepository.save(new Account(null, "John Doe", 1000.0));

        assertThrows(TransactionSystemException.class, () -> {
            accountService.mandatoryTransaction(account.getId(), 300.0);
        });
    }

    @Test
    void testNeverTransactionWithExistingTransaction() {

        final Account account = accountRepository.save(new Account(null, "John Doe", 1000.0));

        assertThrows(TransactionSystemException.class, () -> {
            accountService.neverTransaction(account.getId(), 300.0);
        });
    }

    @Test
    @Rollback
    void testSupportsTransaction() {
        Account account = new Account(null, "John Doe", 1000.0);
        account = accountRepository.save(account);

        accountService.supportsTransaction(account.getId(), 400.0);

        Account updatedAccount = accountRepository.findById(account.getId()).orElseThrow();
        assertEquals(1400.0, updatedAccount.getBalance());

        TransactionLog log = transactionLogRepository.findAll().get(0);
        assertEquals(account.getId(), log.getAccountId());
        assertEquals(400.0, log.getAmount());
        assertEquals("SUPPORTS", log.getPropagationType());
    }
}

