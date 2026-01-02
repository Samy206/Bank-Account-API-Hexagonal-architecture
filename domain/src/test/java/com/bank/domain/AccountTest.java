package com.bank.domain;

import com.bank.domain.enums.AccountType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class AccountTest {

    private final String CASH = "Cash";
    private final LocalDate DATE = LocalDate.now();

    @Test
    @DisplayName("Creating an account without parameters")
    public void accountCreationWithoutParametersTest() {
        Account account = new Account();
        assertNotNull(account.getAccountNumber());
        assertEquals(0, account.getBalance());
    }


    @Test
    @DisplayName("Creating an account with account number")
    public void accountCreationWithAccountNumberTest() {
        String accountNumber = "123456";
        Account account = new Account(accountNumber);
        assertEquals(accountNumber, account.getAccountNumber());
        assertEquals(0, account.getBalance());
    }

    @Test
    @DisplayName("Creating an account with account number and initial balance")
    public void accountCreationWithAccountNumberAndInitialBalanceTest() {
        String accountNumber = "123456";
        float balance = 50;
        Account account = new Account(accountNumber, balance);
        assertEquals(accountNumber, account.getAccountNumber());
        assertEquals(balance, account.getBalance());
    }


    @Test
    @DisplayName("Account deposit going OK")
    public void accountDepositOK() {
        Account account = new Account();
        assertEquals(0, account.getBalance());

        account.deposit(DATE, 50);
        assertEquals(50, account.getBalance());
    }

    @Test
    @DisplayName("Account deposit going KO, throwing exception")
    public void accountDepositKO() {
        Account account = new Account();
        assertEquals(0, account.getBalance());
        assertThrows(IllegalArgumentException.class, () -> account.deposit(DATE, -50));
    }


    @Test
    @DisplayName("Account withdral going OK")
    public void accountWithdrawaltOK() {
        Account account = new Account();
        assertEquals(0, account.getBalance());

        account.deposit(DATE, 50);
        assertEquals(50, account.getBalance());

        account.withdraw(DATE, 45, CASH);
        assertEquals(5, account.getBalance());
    }

    @Test
    @DisplayName("Account withdral going KO, amount > balance or negative")
    public void accountWithdrawalKO() {
        Account account = new Account();
        account.deposit(DATE, 50);
        assertEquals(50, account.getBalance());
        
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(DATE, 60, CASH));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(DATE, -40, CASH));
    }

    @Test
    @DisplayName("Account overwithdral going OK")
    public void accountOverwithdrawalOK() {
        Account account = new Account();
        account.deposit(DATE, 50);
        assertEquals(50, account.getBalance());

        account.setAuthorizedOverdraw(100);
        assertEquals(100, account.getAuthorizedOverdraw());
        
        float balance = account.withdraw(DATE, 70, CASH);
        assertEquals(-20, balance);

        balance = account.withdraw(DATE, 70, CASH);
        assertEquals(-90, balance);

        balance = account.withdraw(DATE, 10, CASH);
        assertEquals(-100, balance);
    }

    @Test
    @DisplayName("Account overwithdral going KO")
    public void accountOverwithdrawalKO() {
        Account account = new Account();
        account.deposit(DATE, 50);
        assertEquals(50, account.getBalance());

        account.setAuthorizedOverdraw(100);
        assertEquals(100, account.getAuthorizedOverdraw());
        
        float balance = account.withdraw(DATE, 70, CASH);
        assertEquals(-20, balance);

        balance = account.withdraw(DATE, 70, CASH);
        assertEquals(-90, balance);

        balance = account.withdraw(DATE, 10, CASH);
        assertEquals(-100, balance);

        assertThrows(IllegalArgumentException.class, () -> account.withdraw(DATE, 10, CASH));
    }

    @Test
    @DisplayName("Getting the operations of the last 30days")
    public void accountGetOperations() {
        Account account = new Account();

        account.deposit(DATE.minusDays(35), 50);
        account.deposit(DATE.minusDays(33), 40);
        account.deposit(DATE.minusDays(25), 20);
        account.withdraw(DATE.minusDays(23), 10, "Starbucks");
        account.deposit(DATE.minusDays(22), 30);
        account.withdraw(DATE.minusDays(15), 22, "League of Legends Skin");
        account.deposit(DATE.minusDays(10), 500);

        MonthlyReview monthlyReview = account.getMonthlyOperations();
        List<Operation> operations = monthlyReview.getOperations();

        assertEquals(500, operations.get(0).getAmount());
        assertEquals(22, operations.get(1).getAmount());
        assertEquals(30, operations.get(2).getAmount());
        assertEquals(10, operations.get(3).getAmount());
        assertEquals(20, operations.get(4).getAmount());

        assertEquals(AccountType.CURRENT_ACCOUNT, monthlyReview.getAccountType());
        
        assertEquals(608, monthlyReview.getBalance());
        
    }

}
