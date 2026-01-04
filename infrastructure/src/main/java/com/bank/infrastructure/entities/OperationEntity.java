package com.bank.infrastructure.entities;

import com.bank.domain.Operation;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;    
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "operation")
public class OperationEntity {

    @Id  
    @GeneratedValue( strategy=GenerationType.SEQUENCE, generator="operation_seq" )
    @SequenceGenerator(
        name = "operation_seq",
        sequenceName = "operation_seq",
        allocationSize = 1
    )
    private Long operationId;

    private LocalDate date;
    private BigDecimal amount;
    private String label;
    
    @JoinColumn(name = "account_number")
    @ManyToOne
    private AccountEntity accountEntity;
    
    public LocalDate getDate() {
        return date;
    }

    public void setAccountEntity(AccountEntity accountEntity) {
        this.accountEntity = accountEntity;
    }

    public Operation toDomain() {
        Operation operation = new Operation(this.date, this.amount, this.label);
        return operation;
    }

    public OperationEntity fromDomain(Operation operation) {
        this.date = operation.getDate();
        this.amount = operation.getAmount();
        this.label = operation.getLabel();
        return this;
    }


}
