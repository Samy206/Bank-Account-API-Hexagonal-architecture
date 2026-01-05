package com.bank.adapters.dto;

import java.math.BigDecimal;
import java.util.List;
import com.bank.domain.enums.AccountType;

/**
 * MonthlyReviewDTO : needed getter and setter to be converted to JSON
 */
public class MonthlyReviewDTO {

    private List<OperationDTO> operations;
    private BigDecimal balance;

    private AccountType accountType;

    public MonthlyReviewDTO(List<OperationDTO> operations, BigDecimal balance, AccountType accountType) {
        this.operations = operations;
        this.balance = balance;
        this.accountType = accountType;
    }

    public List<OperationDTO> getOperations() {
        return operations;
    }

    public void setOperations(List<OperationDTO> operations) {
        this.operations = operations;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
