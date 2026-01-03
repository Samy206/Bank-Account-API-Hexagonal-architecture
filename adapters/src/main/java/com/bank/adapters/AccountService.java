package com.bank.adapters;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.bank.domain.Account;
import com.bank.domain.MonthlyReview;

@Service
public class AccountService {

    private Account account = new Account();

    public String getAccountNumber() {
        return account.getAccountNumber();
    }

    public BigDecimal getBalance() {
        return account.getBalance();
    }

    public BigDecimal getAuthorizedOverdraw() {
        return account.getAuthorizedOverdraw();
    }

    public BigDecimal getOverdraw() {
        return account.getOverdraw();
    }

    public void setAuthorizedOverdraw(BigDecimal authorizedOverdraw) {
        account.setAuthorizedOverdraw(authorizedOverdraw);
    }

    public BigDecimal deposit(LocalDate date, BigDecimal amount) throws IllegalArgumentException {
        return account.deposit(date, amount);
    }


    public BigDecimal withdraw(LocalDate date, BigDecimal amount, String label) throws IllegalArgumentException {
        return account.withdraw(date, amount, label);
    }


    public MonthlyReview getMonthlyOperations() {
        return account.getMonthlyOperations();
    }
}
