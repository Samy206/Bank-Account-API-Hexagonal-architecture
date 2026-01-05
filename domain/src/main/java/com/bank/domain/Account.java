package com.bank.domain;

import java.util.List;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.Comparator;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.bank.domain.enums.AccountType;

public class Account implements Comparable<Account> {

    protected String accountNumber;
    protected BigDecimal balance = BigDecimal.ZERO;
    protected AccountType accountType = AccountType.CURRENT_ACCOUNT;
    protected List<Operation> operations;

    /**
     * Explicit definition of the overdraw
    */
    protected BigDecimal authorizedOverdraw;
    protected BigDecimal overdraw;

    /**
     * Account creation without parameters : we generate a random account number with an epmty balance
    */
    public Account() {
        Integer randomNumber = (int) (( Math.random() + 1 ) * 1000000);
        this.accountNumber = randomNumber.toString();
        this.balance = new BigDecimal(0);
        this.authorizedOverdraw = new BigDecimal(0);
        this.operations = new ArrayList<>();
    }

    /**
     * Account creation with an account number : epmty balance is set
    */
    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = new BigDecimal(0);
        this.authorizedOverdraw = new BigDecimal(0);
        this.operations = new ArrayList<>();
    }

    /**
     * Account creation with an account number and a balance
    */
    public Account(String accountNumber, BigDecimal initialBalance) {
        this.accountNumber = accountNumber;
        this.authorizedOverdraw = new BigDecimal(0);;
        this.operations = new ArrayList<>();
        if(initialBalance.compareTo(BigDecimal.ZERO) > 0) {
            deposit(LocalDate.now(), initialBalance);
        } else {
            this.balance = initialBalance;
        }
    }

    /* Getters and setters */
    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getAuthorizedOverdraw() {
        return authorizedOverdraw;
    }

    public BigDecimal getOverdraw() {
        return overdraw;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setAuthorizedOverdraw(BigDecimal authorizedOverdraw) {
        if (authorizedOverdraw.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("The authorized overdraw have to be positive");
        }
        this.authorizedOverdraw = authorizedOverdraw;
    }

    public void setOverdraw(BigDecimal overdraw) {
        if (overdraw.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("The overdraw have to be positive");
        }
        this.overdraw = overdraw;
    }

    /**
     * Deposit an amount to the account
     * @param amount : The amount must be positive
     * @return The new balance
     * @throws IllegalArgumentException if the amount is not positive
    */
    public BigDecimal deposit(LocalDate date, BigDecimal amount) throws IllegalArgumentException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance = this.balance.add(amount);
 
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
    public BigDecimal withdraw(LocalDate date, BigDecimal amount, String label) throws IllegalArgumentException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0 
            || 
            amount.compareTo(this.balance.add(authorizedOverdraw)) > 0
        ) {
            throw new IllegalArgumentException("Withdraw amount must be positive and inferior to balance");
        }
        this.balance = this.balance.subtract(amount);

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
    public int compareTo(Account account) {
        if (this == account) return 0;
        return account.getBalance().compareTo(balance);
    }

    @Override
    public String toString() {
        return "Account Number: " + this.accountNumber + ", Balance: " + this.balance;
    }

}
