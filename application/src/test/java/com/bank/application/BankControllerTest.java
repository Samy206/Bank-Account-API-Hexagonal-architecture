package com.bank.application;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.bank.BankController;
import com.bank.adapters.dto.OperationDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;


@SpringBootTest(classes = com.bank.BankApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BankControllerTest {

    @Autowired
    private BankController bankController;

    @SuppressWarnings("null")
    @Test
    @DisplayName("Getting account number through controller method call")
    void getAccountNumberTestOK() throws Exception {
        assertNotNull(bankController.getAccountNumber().getBody().get("Account number"));
    }


    @SuppressWarnings("null")
    @Test
    @DisplayName("Getting balance through controller method call")
    void getBalanceTestOK() throws Exception {
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.CEILING), bankController.getBalance().getBody().get("Balance"));
    }


    @SuppressWarnings("null")
    @Test
    @DisplayName("Getting authorized overwithdrawal through controller method call")
    void getOverWithdrawalTestOK() throws Exception {
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.CEILING), bankController.getAuthorizedOverwithdrawal().getBody().get("Authorized overwithdrawal"));
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("put deposit through controller method call")
    void putDepositTestOK() throws Exception {
        OperationDTO operation = new OperationDTO(LocalDate.now(), BigDecimal.valueOf(20), "cash");
        BigDecimal balance = (BigDecimal) bankController.deposit(operation).getBody().get("New balance");
        assertEquals(BigDecimal.valueOf(20).setScale(2, RoundingMode.CEILING), balance);
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("put deposit of an incorrect amount through controller method call")
    void putDepositTestKO() throws Exception {
        OperationDTO operation = new OperationDTO(LocalDate.now(), BigDecimal.valueOf(-20), "cash");
        BigDecimal balance = (BigDecimal) Optional.ofNullable(bankController.deposit(operation).getBody().get("New balance")).orElse(null);
        assertNull(balance);
    }


    @SuppressWarnings("null")
    @Test
    @DisplayName("get withdrawal through controller method call")
    void getWithdrawalTestOK() throws Exception {
        
        OperationDTO operation = new OperationDTO(LocalDate.now(), BigDecimal.valueOf(20), "cash");
        BigDecimal balance = (BigDecimal) bankController.deposit(operation).getBody().get("New balance");
        assertEquals(BigDecimal.valueOf(20).setScale(2, RoundingMode.CEILING), balance);

        
        balance = (BigDecimal) bankController.withdraw(operation).getBody().get("New balance");
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.CEILING), balance);
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("get withdrawal of an incorrect amount through controller method call")
    void getWithdrawalTestKO() throws Exception {
        
        OperationDTO operation = new OperationDTO(LocalDate.now(), BigDecimal.valueOf(0), "cash");
        BigDecimal balance = (BigDecimal) Optional.ofNullable(bankController.withdraw(operation).getBody().get("New balance")).orElse(null);
        assertNull(balance);

        operation = new OperationDTO(LocalDate.now(), BigDecimal.valueOf(-10), "cash");
        balance = (BigDecimal) Optional.ofNullable(bankController.withdraw(operation).getBody().get("New balance")).orElse(null);
        assertNull(balance);

        operation = new OperationDTO(LocalDate.now(), BigDecimal.valueOf(20), "cash");
        balance = (BigDecimal) Optional.ofNullable(bankController.withdraw(operation).getBody().get("New balance")).orElse(null);
        assertNull(balance);
       
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("update overwithdrawal through api call OK, and check that the update has been made")
    void putOverwithdrawalTestOK() throws Exception {
        BigDecimal authorizedOverwithdrawal = (BigDecimal) bankController.getAuthorizedOverwithdrawal().getBody().get("Authorized overwithdrawal");
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.CEILING), authorizedOverwithdrawal);

        authorizedOverwithdrawal = (BigDecimal) bankController.changeAuthorizedWithdrawal(BigDecimal.valueOf(100)).getBody().get("New authorized overwithdrawal");
        assertEquals(BigDecimal.valueOf(100).setScale(2, RoundingMode.CEILING), authorizedOverwithdrawal);

        authorizedOverwithdrawal = (BigDecimal) bankController.getAuthorizedOverwithdrawal().getBody().get("Authorized overwithdrawal");
        assertEquals(BigDecimal.valueOf(100).setScale(2, RoundingMode.CEILING), authorizedOverwithdrawal);
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("update overwithdrawal through api call KO")
    void putOverwithdrawalTestKO() throws Exception {
        BigDecimal authorizedOverwithdrawal = (BigDecimal) bankController.getAuthorizedOverwithdrawal().getBody().get("Authorized overwithdrawal");
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.CEILING), authorizedOverwithdrawal);

        authorizedOverwithdrawal = (BigDecimal) Optional.ofNullable(bankController.changeAuthorizedWithdrawal(BigDecimal.valueOf(-100)).getBody().get("New authorized overwithdrawal")).orElse(null);
        assertNull(authorizedOverwithdrawal);

        authorizedOverwithdrawal = (BigDecimal) bankController.getAuthorizedOverwithdrawal().getBody().get("Authorized overwithdrawal");
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.CEILING), authorizedOverwithdrawal);

    }
}