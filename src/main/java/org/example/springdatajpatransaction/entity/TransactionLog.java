package org.example.springdatajpatransaction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TransactionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;

    private Double amount;

    private String propagationType;

    public TransactionLog() {}

    public TransactionLog(Long accountId, Double amount, String propagationType) {
        this.accountId = accountId;
        this.amount = amount;
        this.propagationType = propagationType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPropagationType() {
        return propagationType;
    }

    public void setPropagationType(String propagationType) {
        this.propagationType = propagationType;
    }
}

