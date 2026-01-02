package com.bank.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.bank.domain.enums.AccountType;

public class SavingAccount extends Account{
    
    private BigDecimal maxBalance;

    /**
     * Creating a saving account with a max balance
     * @param maxBalance
     */
    public SavingAccount(BigDecimal maxBalance) {
        super();
        this.maxBalance = maxBalance;
    }

    public void setMaxBalance(BigDecimal maxBalance) {
        this.maxBalance = maxBalance;
    }

    public BigDecimal getMaxBalance() {
        return maxBalance;
    }


    /**
     * For a saving account, no overwithdrawal can be authorized
     * @param authorizedOverdraw
     */
    @Override
    public void setAuthorizedOverdraw(BigDecimal authorizedOverdraw) {
        if (authorizedOverdraw != BigDecimal.ZERO) {
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
    public BigDecimal deposit(LocalDate date, BigDecimal amount) throws IllegalArgumentException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        } else if ( (balance.add(amount)) .compareTo(maxBalance) > 0) {
            throw new IllegalArgumentException("The max balance will be exceeded");
        }

        this.balance = this.balance.add(amount);

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
    public BigDecimal withdraw(LocalDate date, BigDecimal amount, String label) throws IllegalArgumentException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0 
            || 
            amount.compareTo(this.balance) > 0
        ) {
            throw new IllegalArgumentException("Withdraw amount must be positive and inferior to balance");
        }

        Operation operation = new Operation(date, amount, label);
        operations.add(operation);

        this.balance = this.balance.subtract(amount);
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
