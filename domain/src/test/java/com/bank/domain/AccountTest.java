package com.bank.domain;

import com.bank.domain.enums.AccountType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountTest {

    private final String CASH = "Cash";
    private final LocalDate DATE = LocalDate.now();

    @Test
    @DisplayName("Creating an account without parameters")
    public void accountCreationWithoutParametersTest() {
        Account account = new Account();
        assertNotNull(account.getAccountNumber());
        assertEquals(BigDecimal.ZERO, account.getBalance());
    }


    @Test
    @DisplayName("Creating an account with account number")
    public void accountCreationWithAccountNumberTest() {
        String accountNumber = "123456";
        Account account = new Account(accountNumber);
        assertEquals(accountNumber, account.getAccountNumber());
        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test
    @DisplayName("Creating an account with account number and initial balance")
    public void accountCreationWithAccountNumberAndInitialBalanceTest() {
        String accountNumber = "123456";
        BigDecimal balance = BigDecimal.valueOf(50);
        Account account = new Account(accountNumber, balance);
        assertEquals(accountNumber, account.getAccountNumber());
        assertEquals(balance, account.getBalance());
    }


    @Test
    @DisplayName("Account deposit going OK")
    public void accountDepositOK() {
        Account account = new Account();
        assertEquals(BigDecimal.ZERO, account.getBalance());

        account.deposit(DATE, BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(50), account.getBalance());
    }

    @Test
    @DisplayName("Account deposit going KO, throwing exception")
    public void accountDepositKO() {
        Account account = new Account();
        assertEquals(BigDecimal.ZERO, account.getBalance());
        assertThrows(IllegalArgumentException.class, () -> account.deposit(DATE, BigDecimal.valueOf(-50)));
    }


    @Test
    @DisplayName("Account withdral going OK")
    public void accountWithdrawaltOK() {
        Account account = new Account();
        assertEquals(BigDecimal.ZERO, account.getBalance());

        account.deposit(DATE, BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(50), account.getBalance());

        account.withdraw(DATE, BigDecimal.valueOf(45), CASH);
        assertEquals(BigDecimal.valueOf(5), account.getBalance());
    }

    @Test
    @DisplayName("Account withdral going KO, amount > balance or negative")
    public void accountWithdrawalKO() {
        Account account = new Account();
        account.deposit(DATE, BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(50), account.getBalance());
        
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(DATE, BigDecimal.valueOf(60), CASH));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(DATE, BigDecimal.valueOf(-40), CASH));
    }

    @Test
    @DisplayName("Account overwithdral going OK")
    public void accountOverwithdrawalOK() {
        Account account = new Account();
        account.deposit(DATE, BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(50), account.getBalance());

        account.setAuthorizedOverdraw(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(100), account.getAuthorizedOverdraw());
        
        BigDecimal balance = account.withdraw(DATE, BigDecimal.valueOf(70), CASH);
        assertEquals(BigDecimal.valueOf(-20), balance);

        balance = account.withdraw(DATE, BigDecimal.valueOf(70), CASH);
        assertEquals(BigDecimal.valueOf(-90), balance);

        balance = account.withdraw(DATE, BigDecimal.valueOf(10), CASH);
        assertEquals(BigDecimal.valueOf(-100), balance);
    }

    @Test
    @DisplayName("Account overwithdral going KO")
    public void accountOverwithdrawalKO() {
        Account account = new Account();
        account.deposit(DATE, BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(50), account.getBalance());

        account.setAuthorizedOverdraw(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(100), account.getAuthorizedOverdraw());
        
        BigDecimal balance = account.withdraw(DATE, BigDecimal.valueOf(70), CASH);
        assertEquals(BigDecimal.valueOf(-20), balance);

        balance = account.withdraw(DATE, BigDecimal.valueOf(70), CASH);
        assertEquals(BigDecimal.valueOf(-90), balance);

        balance = account.withdraw(DATE, BigDecimal.valueOf(10), CASH);
        assertEquals(BigDecimal.valueOf(-100), balance);

        assertThrows(IllegalArgumentException.class, () -> account.withdraw(DATE, BigDecimal.valueOf(10), CASH));
    }

    @Test
    @DisplayName("Getting the operations of the last 30days")
    public void accountGetOperations() {
        Account account = new Account();

        account.deposit(DATE.minusDays(35), BigDecimal.valueOf(50));
        account.deposit(DATE.minusDays(33), BigDecimal.valueOf(40));
        account.deposit(DATE.minusDays(25), BigDecimal.valueOf(20));
        account.withdraw(DATE.minusDays(23), BigDecimal.valueOf(10), "Starbucks");
        account.deposit(DATE.minusDays(22), BigDecimal.valueOf(30));
        account.withdraw(DATE.minusDays(15), BigDecimal.valueOf(22), "League of Legends Skin");
        account.deposit(DATE.minusDays(10), BigDecimal.valueOf(500));

        MonthlyReview monthlyReview = account.getMonthlyOperations();
        List<Operation> operations = monthlyReview.getOperations();

        assertEquals(BigDecimal.valueOf(500), operations.get(0).getAmount());
        assertEquals(BigDecimal.valueOf(22), operations.get(1).getAmount());
        assertEquals(BigDecimal.valueOf(30), operations.get(2).getAmount());
        assertEquals(BigDecimal.valueOf(10), operations.get(3).getAmount());
        assertEquals(BigDecimal.valueOf(20), operations.get(4).getAmount());

        assertEquals(AccountType.CURRENT_ACCOUNT, monthlyReview.getAccountType());
        
        assertEquals(BigDecimal.valueOf(608), monthlyReview.getBalance());
        
    }

}
