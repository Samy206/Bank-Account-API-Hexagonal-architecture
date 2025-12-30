package com.kata.bank.components;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.kata.bank.enums.AccountType;

public class SavingAccount extends Account{
    
    private float maxBalance;

    /**
     * Creating a saving account with a max balance
     * @param maxBalance
     */
    public SavingAccount(float maxBalance) {
        super();
        this.maxBalance = maxBalance;
    }

    public void setMaxBalance(float maxBalance) {
        this.maxBalance = maxBalance;
    }

    public float getMaxBalance() {
        return maxBalance;
    }


    /**
     * For a saving account, no overwithdrawal can be authorized
     * @param authorizedOverdraw
     */
    @Override
    public void setAuthorizedOverdraw(float authorizedOverdraw) {
        if (authorizedOverdraw != 0) {
            throw new IllegalArgumentException("No authorized overwithdawal with asaving account");
        }
        this.authorizedOverdraw = authorizedOverdraw;
    }

    /**
     * Deposit an amount to the account
     * @param amount : The amount must be positive and not exceed the max limit
     * @return The new balance
     * @throws IllegalArgumentException if the amount is not positive or if it exceeds the max limit
     */
    @Override
    public float deposit(LocalDate date, float amount) throws IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        } else if ( (balance + amount) > maxBalance) {
            throw new IllegalArgumentException("The max balance will be exceeded");
        }

        this.balance += amount;

        Operation operation = new Operation(date, amount, "New deposit");
        operations.add(operation);

        
        return this.balance;
    }
    
    /**
     * Withdraw an amount to the account
     * @param amount : The amount must be positive and inferior to the balance
     * @return The new balance
     * @throws IllegalArgumentException if the amount is not positive or superior to the balance
    */
    @Override
    public float withdraw(LocalDate date, float amount, String label) throws IllegalArgumentException {
        if (amount <= 0 
            || 
            amount > this.balance
        ) {
            throw new IllegalArgumentException("Withdraw amount must be positive and inferior to balance");
        }

        Operation operation = new Operation(date, amount, label);
        operations.add(operation);

        this.balance -= amount;
        return this.balance;
    }

    /**
     * Sort list in a reverse order comparing dates of operations, and returning the operations of the last 30 days
     * @return List<Operation>
     */
    @Override
    public MonthlyReview getMonthlyOperations() {
        operations.sort(Comparator.comparing(Operation::getDate).reversed());
        List<Operation> monthlyOperations=  operations.stream().filter(o -> o.getDate().isAfter(LocalDate.now().minusDays(30))).collect(Collectors.toList());
        
        return new MonthlyReview(monthlyOperations, balance, AccountType.SAVING_ACCOUNT);
    }


}
