package com.bank;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.domain.Account;
import com.bank.domain.MonthlyReview;
import com.bank.domain.Operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("app/v1/bank/account")
public class BankController {
    
    private Account bankAccount = new Account();
    private static final Logger LOGGER = LoggerFactory.getLogger(BankController.class);
    /**
     * Get request : searching for account number
     * @return JSON entity with the account number
     */
    @GetMapping(value = "/accountNumber", headers = "X-API-VERSION=1")
    public ResponseEntity<Map<String, Object>> getAccountNumber() {
        LOGGER.info("Account number asked and sent successfully");
        return ResponseEntity.ok(
            Map.of("Account number", bankAccount.getAccountNumber())
        );
        
    }

    /**
     * Get request : searching for the monthly review
     * @return JSON entity with the monthly review
     */
    @GetMapping(value = "/monthlyReview", headers = "X-API-VERSION=1")
    public ResponseEntity<MonthlyReview> getMonthlyReview() {
        LOGGER.info("Monthly review asked and sent successfully");
        return ResponseEntity.ok(bankAccount.getMonthlyOperations());
    }

    /**
     * Get request : searching for the balance
     * @return JSON entity with the balance
     */
    @GetMapping(value = "/balance", headers = "X-API-VERSION=1")
    public ResponseEntity<Map<String, Object>> getBalance() {
        LOGGER.info("Balance asked and sent successfully");
        return ResponseEntity.ok(
            Map.of("Balance", bankAccount.getBalance())
        );
    }

    /**
     * Get request : searching for the authorized overwithdrawal
     * @return JSON entity with the authorized overwithdrawal
     */
    @GetMapping(value = "/authorizedOverwithdrawal", headers = "X-API-VERSION=1")
    public ResponseEntity<Map<String, Object>> getAuthorizedOverwithdrawal() {
        LOGGER.info("Authorized overwithdrawal asked and sent successfully");
        return ResponseEntity.ok(
            Map.of("Authorized overwithdrawal", bankAccount.getAuthorizedOverdraw())
        );
    }


    /**
     * Patch request : Patchting money and updating balance
     * If the date is not specified, it's gonna be filled as today
     * @return JSON entity with the state of the process error/success
     */
    @PatchMapping(value = "/deposit", headers = "X-API-VERSION=1")
    public ResponseEntity<Map<String, Object>> deposit(@RequestBody(required = true) Operation operation) {

        try {
            bankAccount.deposit(operation.getDate(), operation.getAmount());
            LOGGER.info("Deposit of {} made successfully", operation.getAmount());
        } catch (IllegalArgumentException e) {
            LOGGER.error("Deposit failed due to invalid amount: {}", operation.getAmount());
            return ResponseEntity.status(400).body(
                Map.of(
                "Error", "The amount you entered is either null or negative"
                )    
            );
        }
        
        return ResponseEntity.ok(
            Map.of(
                "New balance", bankAccount.getBalance(),
                "Message", "Deposit made successfully"
            )    
        );
    }


    /**
     * Patch request : withdrawing money and updating balance
     * If the date is not specified, it's gonna be filled as today
     * @return JSON entity with the state of the process error/success
     */
    @PatchMapping(value = "/withdraw", headers = "X-API-VERSION=1")
    public ResponseEntity<Map<String, Object>> withdraw(@RequestBody (required = true) Operation operation) {

        try {
            bankAccount.withdraw(operation.getDate(), operation.getAmount(), operation.getLabel());
            LOGGER.info("Withdrawal of {} made successfully", operation.getAmount());
        } catch (IllegalArgumentException e) {
            LOGGER.error("Withdrawal failed due to invalid amount: {}", operation.getAmount());
            return ResponseEntity.status(400).body(
                Map.of(
                "Error", "The amount you entered is either null, negative, or greater than the actual balance"
                )    
            );
        }
        
        return ResponseEntity.ok(
            Map.of(
                "New balance", bankAccount.getBalance(),
                "Message", "Withdrawal made successfully"
            )    
        );
    }


    /**
     * Patch request : updating the overwithdrawal limit
     * @return JSON entity with the state of the process error/success
     */
    @PatchMapping(value = "/authorizedOverwithdrawal", headers = "X-API-VERSION=1")
    public ResponseEntity<Map<String, Object>> changeAuthorizedWithdrawal(@RequestBody(required = true) BigDecimal amount) {

        try {
            bankAccount.setAuthorizedOverdraw(amount);
            LOGGER.info("Authorized overwithdrawal updated successfully to {}", amount);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Failed to update authorized overwithdrawal due to invalid amount: {}", amount);
            return ResponseEntity.status(400).body(
                Map.of(
                "Error", "The amount you entered is negative"
                )    
            );
        }
        
        return ResponseEntity.ok(
            Map.of(
                "New authorized overwithdrawal", bankAccount.getAuthorizedOverdraw(),
                "Message", "Overwithdrawal updated successfully"
            )    
        );
    }



}
