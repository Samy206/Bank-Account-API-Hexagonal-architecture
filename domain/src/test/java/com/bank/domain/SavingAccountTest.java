package com.bank.domain;

import com.bank.domain.enums.AccountType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SavingAccountTest {

    private final String CASH = "Cash";
    private final LocalDate DATE = LocalDate.now();

    @Test
    @DisplayName("Creating a saving account with a max balance, and withdraw a part of it")
    public void savingAccountWithdrawalOK() {
        SavingAccount savingAccount = new SavingAccount(100);
        assertEquals(100, savingAccount.getMaxBalance());

        double balance = savingAccount.deposit(DATE, 70);
        assertEquals(70, balance);

        balance = savingAccount.withdraw(DATE, 50, CASH);
        assertEquals(20, balance);

    }

    @Test
    @DisplayName("Creating a saving account with a max balance, and deposit more then authorized")
    public void savingAccountDepositKO() {
        SavingAccount savingAccount = new SavingAccount(100);
        assertEquals(100, savingAccount.getMaxBalance());
        assertThrows(IllegalArgumentException.class, () -> savingAccount.deposit(DATE, 120));

    }

    @Test
    @DisplayName("Creating a saving account with a max balance, and withdraw more than authorized")
    public void savingAccountWithdrawalKO() {
        SavingAccount savingAccount = new SavingAccount(100);
        assertEquals(100, savingAccount.getMaxBalance());

        double balance = savingAccount.deposit(DATE, 70);
        assertEquals(70, balance);

        assertThrows(IllegalArgumentException.class, () -> savingAccount.withdraw(DATE, 80, CASH));

    }

    @Test
    @DisplayName("Getting the operations of the last 30days")
    public void accountGetOperations() {
        SavingAccount account = new SavingAccount(1000);

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

        assertEquals(AccountType.SAVING_ACCOUNT, monthlyReview.getAccountType());
        
        assertEquals(608, monthlyReview.getBalance());
        
    }


}
