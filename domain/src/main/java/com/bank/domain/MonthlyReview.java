package com.bank.domain;
import java.util.List;
import com.bank.domain.enums.AccountType;

public class MonthlyReview {

    private List<Operation> operations;
    private double balance;

    private AccountType accountType;

    public MonthlyReview(List<Operation> operations, double balance, AccountType accountType) {
        this.operations = operations;
        this.balance = balance;
        this.accountType = accountType;
    }


    public List<Operation> getOperations() {
        return operations;
    }

    public double getBalance() {
        return balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

}
