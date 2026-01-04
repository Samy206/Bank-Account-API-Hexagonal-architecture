package com.bank.infrastructure.entities;

import com.bank.domain.Account;
import java.math.BigDecimal;
import com.bank.domain.enums.AccountType;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;    
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;  
import jakarta.persistence.Id;  
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;


@Entity
@Table(name = "account")
public class AccountEntity {


    @Id  
    @GeneratedValue( strategy=GenerationType.IDENTITY )
    protected String accountNumber;
    protected BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    protected AccountType accountType;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn( name="account_number")
    protected List<OperationEntity> operations;

    protected BigDecimal authorizedOverdraw;
    protected BigDecimal overdraw;

    public void addOperation(OperationEntity operationEntity) {
        if(operations == null) {
            operations = new ArrayList<OperationEntity>();
        }
        operations.add(operationEntity);
    }

    public Account toDomain() {
        Account account = new Account(this.accountNumber, this.balance);
        account.setAccountType(this.accountType);
        account.setAuthorizedOverdraw(this.authorizedOverdraw);
        account.setOverdraw(this.overdraw);
        return account;
    }

    public AccountEntity fromDomain(Account account) {
        this.accountNumber = account.getAccountNumber();
        this.balance = account.getBalance();
        this.accountType = account.getAccountType();
        this.authorizedOverdraw = account.getAuthorizedOverdraw();
        this.overdraw = account.getOverdraw();
        return this;
    }

}
