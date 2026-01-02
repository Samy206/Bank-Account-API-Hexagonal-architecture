package com.bank.domain;

import com.bank.domain.enums.AccountType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SavingAccountTest {

    private final String CASH = "Cash";
    private final LocalDate DATE = LocalDate.now();

    @Test
    @DisplayName("Creating a saving account with a max balance, and withdraw a part of it")
    void savingAccountWithdrawalOK() {
        SavingAccount savingAccount = new SavingAccount(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(100), savingAccount.getMaxBalance());

        BigDecimal balance = savingAccount.deposit(DATE, BigDecimal.valueOf(70));
        assertEquals(BigDecimal.valueOf(70), balance);

        balance = savingAccount.withdraw(DATE, BigDecimal.valueOf(50), CASH);
        assertEquals(BigDecimal.valueOf(20), balance);

    }

    @Test
    @DisplayName("Creating a saving account with a max balance, and deposit more then authorized")
    void savingAccountDepositKO() {
        SavingAccount savingAccount = new SavingAccount(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(100), savingAccount.getMaxBalance());
        assertThrows(IllegalArgumentException.class, () -> savingAccount.deposit(DATE, BigDecimal.valueOf(120)));

    }

    @Test
    @DisplayName("Creating a saving account with a max balance, and withdraw more than authorized")
    void savingAccountWithdrawalKO() {
        SavingAccount savingAccount = new SavingAccount(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(100), savingAccount.getMaxBalance());

        BigDecimal balance = savingAccount.deposit(DATE, BigDecimal.valueOf(70));
        assertEquals(BigDecimal.valueOf(70), balance);

        assertThrows(IllegalArgumentException.class, () -> savingAccount.withdraw(DATE, BigDecimal.valueOf(80), CASH));

    }

    @Test
    @DisplayName("Getting the operations of the last 30days")
    void accountGetOperations() {
        SavingAccount account = new SavingAccount(BigDecimal.valueOf(1000));

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

        assertEquals(AccountType.SAVING_ACCOUNT, monthlyReview.getAccountType());
        
        assertEquals(BigDecimal.valueOf(608), monthlyReview.getBalance());
        
    }


}
