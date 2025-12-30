package com.kata.bank.components;

import java.util.List;
import java.util.stream.Collectors;

import com.kata.bank.enums.AccountType;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Comparator;

import java.time.LocalDate;
import org.springframework.stereotype.Component;

// Using @Component to make an easy testing project with one account
@Component
public class Account implements Comparable {

    @NotNull
    protected String accountNumber;
    protected float balance;

    @NotNull
    protected List<Operation> operations;

    /*
     * Explicit definition of the overdraw
    */
    protected float authorizedOverdraw;
    protected float overdraw;

    /**
     * Account creation without parameters : we generate a random account number with an epmty balance
    */
    public Account() {
        Integer randomNumber = (int) (( Math.random() + 1 ) * 1000000);
        this.accountNumber = randomNumber.toString();
        this.balance = 0;
        this.authorizedOverdraw = 0;
        this.operations = new ArrayList<>();
    }

    /**
     * Account creation with an account number : epmty balance is set
    */
    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = 0;
        this.authorizedOverdraw = 0;
        this.operations = new ArrayList<>();
    }

    /**
     * Account creation with an account number and a balance
    */
    public Account(String accountNumber, float initialBalance) {
        this.accountNumber = accountNumber;
        this.authorizedOverdraw = 0;
        this.operations = new ArrayList<>();
        deposit(LocalDate.now(), initialBalance);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public float getBalance() {
        return balance;
    }

    public float getAuthorizedOverdraw() {
        return authorizedOverdraw;
    }

    public float getOverdraw() {
        return overdraw;
    }

    /**
     * Setting up authorized overwithdrawal
     * @param authorizedOverdraw
     */
    public void setAuthorizedOverdraw(float authorizedOverdraw) {
        if (authorizedOverdraw < 0) {
            throw new IllegalArgumentException("The authorized overdraw have to be positive");
        }
        this.authorizedOverdraw = authorizedOverdraw;
    }

    /**
     * Deposit an amount to the account
     * @param amount : The amount must be positive
     * @return The new balance
     * @throws IllegalArgumentException if the amount is not positive
    */
    public float deposit(LocalDate date, float amount) throws IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance += amount;
 
        Operation operation = new Operation(date, amount, "New deposit");
        operations.add(operation);

        return this.balance;
    }   

    /**
     * Withdraw an amount to the account
     * @param amount : The amount must be positive and inferior to the balance (plus de the authorized overdrawal)
     * @return The new balance
     * @throws IllegalArgumentException if the amount is not positive or superior to the balance (plus de the authorized overdrawal)
    */
    public float withdraw(LocalDate date, float amount, String label) throws IllegalArgumentException {
        if (amount <= 0 
            || 
            amount > ( this.balance + authorizedOverdraw) 
        ) {
            throw new IllegalArgumentException("Withdraw amount must be positive and inferior to balance");
        }
        this.balance -= amount;

        Operation operation = new Operation(date, amount, label);
        operations.add(operation);

        return this.balance;
    }

    /**
     * Sort list in a reverse order comparing dates of operations, and returning the operations of the last 30 days
     * @return List<Operation>
     */
    public MonthlyReview getMonthlyOperations() {
        operations.sort(Comparator.comparing(Operation::getDate).reversed());
        List<Operation> monthlyOperations=  operations.stream().filter(o -> o.getDate().isAfter(LocalDate.now().minusDays(30))).collect(Collectors.toList());
        
        return new MonthlyReview(monthlyOperations, balance, AccountType.CURRENT_ACCOUNT);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Account account = (Account) obj;
        return accountNumber.equals(account.accountNumber);
    }

    @Override
    public int compareTo(Object obj) {
        if (this == obj) return 0;
        if (obj == null || getClass() != obj.getClass()) return -1;
        Account account = (Account) obj;
        return account.getBalance() > this.balance ? 1 : (account.getBalance() < this.balance ? -1 : 0);
    }

    @Override
    public String toString() {
        return "Account Number: " + this.accountNumber + ", Balance: " + this.balance;
    }

}
