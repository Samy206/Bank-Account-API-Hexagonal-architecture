package com.bank.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bank.domain.MonthlyReview;
import com.bank.domain.Operation;
import com.bank.domain.enums.AccountType;

public class AccountServiceTest {

    private final String CASH = "Cash";
    private final LocalDate DATE = LocalDate.now();

    @Test
    @DisplayName("Creating an account without parameters")
    void accountCreationWithoutParametersTest() {
        AccountService accountService = new AccountService();
        assertNotNull(accountService.getAccountNumber());
        assertEquals(BigDecimal.ZERO, accountService.getBalance());
    }


    @Test
    @DisplayName("Creating an account with account number and initial balance")
    void accountCreationWithAccountNumberAndInitialBalanceTest() {
        BigDecimal balance = BigDecimal.valueOf(50);
        AccountService accountService = new AccountService();
        accountService.deposit(DATE, balance);
        assertEquals(balance, accountService.getBalance());
    }


    @Test
    @DisplayName("Account deposit going OK")
    void accountDepositOK() {
        AccountService accountService = new AccountService();
        assertEquals(BigDecimal.ZERO, accountService.getBalance());

        accountService.deposit(DATE, BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(50), accountService.getBalance());
    }

    @Test
    @DisplayName("Account deposit going KO, throwing exception")
    void accountDepositKO() {
        AccountService accountService = new AccountService();
        assertEquals(BigDecimal.ZERO, accountService.getBalance());
        assertThrows(IllegalArgumentException.class, () -> accountService.deposit(DATE, BigDecimal.valueOf(-50)));
    }


    @Test
    @DisplayName("Account withdral going OK")
    void accountWithdrawaltOK() {
        AccountService accountService = new AccountService();
        assertEquals(BigDecimal.ZERO, accountService.getBalance());

        accountService.deposit(DATE, BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(50), accountService.getBalance());
        accountService.withdraw(DATE, BigDecimal.valueOf(45), CASH);
        assertEquals(BigDecimal.valueOf(5), accountService.getBalance());
    }

    @Test
    @DisplayName("Account withdral going KO, amount > balance or negative")
    void accountWithdrawalKO() {
        AccountService accountService = new AccountService();
        accountService.deposit(DATE, BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(50), accountService.getBalance());

        assertThrows(IllegalArgumentException.class, () -> accountService.withdraw(DATE, BigDecimal.valueOf(60), CASH));
        assertThrows(IllegalArgumentException.class, () -> accountService.withdraw(DATE, BigDecimal.valueOf(-40), CASH));
    }

    @Test
    @DisplayName("Account overwithdral going OK")
    void accountOverwithdrawalOK() {
        AccountService accountService = new AccountService();
        accountService.deposit(DATE, BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(50), accountService.getBalance());

        accountService.setAuthorizedOverdraw(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(100), accountService.getAuthorizedOverdraw());

        BigDecimal balance = accountService.withdraw(DATE, BigDecimal.valueOf(70), CASH);
        assertEquals(BigDecimal.valueOf(-20), balance);

        balance = accountService.withdraw(DATE, BigDecimal.valueOf(70), CASH);
        assertEquals(BigDecimal.valueOf(-90), balance);

        balance = accountService.withdraw(DATE, BigDecimal.valueOf(10), CASH);
        assertEquals(BigDecimal.valueOf(-100), balance);
    }

    @Test
    @DisplayName("Account overwithdral going KO")
    void accountOverwithdrawalKO() {
        AccountService accountService = new AccountService();
        accountService.deposit(DATE, BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(50), accountService.getBalance());

        accountService.setAuthorizedOverdraw(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(100), accountService.getAuthorizedOverdraw());
        
        BigDecimal balance = accountService.withdraw(DATE, BigDecimal.valueOf(70), CASH);
        assertEquals(BigDecimal.valueOf(-20), balance);

        balance = accountService.withdraw(DATE, BigDecimal.valueOf(70), CASH);
        assertEquals(BigDecimal.valueOf(-90), balance);

        balance = accountService.withdraw(DATE, BigDecimal.valueOf(10), CASH);
        assertEquals(BigDecimal.valueOf(-100), balance);

        assertThrows(IllegalArgumentException.class, () -> accountService.withdraw(DATE, BigDecimal.valueOf(10), CASH));
    }

    @Test
    @DisplayName("Getting the operations of the last 30days")
    void accountGetOperations() {
        AccountService accountService = new AccountService();

        accountService.deposit(DATE.minusDays(35), BigDecimal.valueOf(50));
        accountService.deposit(DATE.minusDays(33), BigDecimal.valueOf(40));
        accountService.deposit(DATE.minusDays(25), BigDecimal.valueOf(20));
        accountService.withdraw(DATE.minusDays(23), BigDecimal.valueOf(10), "Starbucks");
        accountService.deposit(DATE.minusDays(22), BigDecimal.valueOf(30));
        accountService.withdraw(DATE.minusDays(15), BigDecimal.valueOf(22), "League of Legends Skin");
        accountService.deposit(DATE.minusDays(10), BigDecimal.valueOf(500));

        MonthlyReview monthlyReview = accountService.getMonthlyOperations();
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
