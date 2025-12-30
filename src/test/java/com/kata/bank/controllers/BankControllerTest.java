package com.kata.bank.controllers;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.kata.bank.components.Operation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.Optional;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BankControllerTest {

    @Autowired
    private BankController bankController;

    @Test
    @DisplayName("Getting account number through controller method call")
    void getAccountNumberTestOK() throws Exception {
        assertNotNull(bankController.getAccountNumber().getBody().get("Account number"));
    }


    @Test
    @DisplayName("Getting balance through controller method call")
    void getBalanceTestOK() throws Exception {
        assertEquals((float) 0.0, bankController.getBalance().getBody().get("Balance"));
    }


    @Test
    @DisplayName("Getting authorized overwithdrawal through controller method call")
    void getOverWithdrawalTestOK() throws Exception {
        assertEquals((float) 0.0, bankController.getAuthorizedOverwithdrawal().getBody().get("Authorized overwithdrawal"));
    }


    @Test
    @DisplayName("put deposit through controller method call")
    void putDepositTestOK() throws Exception {
        Operation operation = new Operation(LocalDate.now(), 20, "cash");
        float balance = (float) bankController.deposit(operation).getBody().get("New balance");
        assertEquals((float) 20, balance);
    }

    @Test
    @DisplayName("put deposit of an incorrect amount through controller method call")
    void putDepositTestKO() throws Exception {
        Operation operation = new Operation(LocalDate.now(), -20, "cash");
        Float balance = (Float) Optional.ofNullable(bankController.deposit(operation).getBody().get("New balance")).orElse(null);
        assertNull(balance);
    }


    @Test
    @DisplayName("get withdrawal through controller method call")
    void getWithdrawalTestOK() throws Exception {
        
        Operation operation = new Operation(LocalDate.now(), 20, "cash");
        float balance = (float) bankController.deposit(operation).getBody().get("New balance");
        assertEquals(20, balance);

        
        balance = (float) bankController.withdraw(operation).getBody().get("New balance");
        assertEquals(0, balance);
    }

    @Test
    @DisplayName("get withdrawal of an incorrect amount through controller method call")
    void getWithdrawalTestKO() throws Exception {
        
        Operation operation = new Operation(LocalDate.now(), 0, "cash");
        Float balance = (Float) Optional.ofNullable(bankController.withdraw(operation).getBody().get("New balance")).orElse(null);
        assertNull(balance);

        operation = new Operation(LocalDate.now(), -10, "cash");
        balance = (Float) Optional.ofNullable(bankController.withdraw(operation).getBody().get("New balance")).orElse(null);
        assertNull(balance);

        operation = new Operation(LocalDate.now(), 20, "cash");
        balance = (Float) Optional.ofNullable(bankController.withdraw(operation).getBody().get("New balance")).orElse(null);
        assertNull(balance);
       
    }

    @Test
    @DisplayName("update overwithdrawal through api call OK, and check that the update has been made")
    void putOverwithdrawalTestOK() throws Exception {
        Float authorizedOverwithdrawal = (Float) bankController.getAuthorizedOverwithdrawal().getBody().get("Authorized overwithdrawal");
        assertEquals(0, authorizedOverwithdrawal);

        authorizedOverwithdrawal = (Float) bankController.changeAuthorizedWithdrawal(100).getBody().get("New authorized overwithdrawal");
        assertEquals(100, authorizedOverwithdrawal);

        authorizedOverwithdrawal = (Float) bankController.getAuthorizedOverwithdrawal().getBody().get("Authorized overwithdrawal");
        assertEquals(100, authorizedOverwithdrawal);
    }

    @Test
    @DisplayName("update overwithdrawal through api call KO")
    void putOverwithdrawalTestKO() throws Exception {
        
        Float authorizedOverwithdrawal = (Float) bankController.getAuthorizedOverwithdrawal().getBody().get("Authorized overwithdrawal");
        assertEquals(0, authorizedOverwithdrawal);

        authorizedOverwithdrawal = (Float) Optional.ofNullable(bankController.changeAuthorizedWithdrawal(-100).getBody().get("New authorized overwithdrawal")).orElse(null);
        assertNull(authorizedOverwithdrawal);

        authorizedOverwithdrawal = (Float) bankController.getAuthorizedOverwithdrawal().getBody().get("Authorized overwithdrawal");
        assertEquals(0, authorizedOverwithdrawal);

    }
}
