package com.bank;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.domain.Account;
import com.bank.domain.MonthlyReview;
import com.bank.domain.Operation;

@RestController
@RequestMapping("bank/account")
public class BankController {

    @Autowired
    private Account bankAccount;

    /**
     * Get request : searching for account number
     * @return JSON entity with the account number
     */
    @GetMapping("/accountNumber")
    public ResponseEntity<Map<String, Object>> getAccountNumber() {
        return ResponseEntity.ok(
            Map.of("Account number", bankAccount.getAccountNumber())
        );
    }

    /**
     * Get request : searching for the monthly review
     * @return JSON entity with the monthly review
     */
    @GetMapping("/monthlyReview")
    public ResponseEntity<MonthlyReview> getMonthlyReview() {
        return ResponseEntity.ok(bankAccount.getMonthlyOperations());
    }

    /**
     * Get request : searching for the balance
     * @return JSON entity with the balance
     */
    @GetMapping("/balance")
    public ResponseEntity<Map<String, Object>> getBalance() {
        return ResponseEntity.ok(
            Map.of("Balance", bankAccount.getBalance())
        );
    }

    /**
     * Get request : searching for the authorized overwithdrawal
     * @return JSON entity with the authorized overwithdrawal
     */
    @GetMapping("/authorizedOverwithdrawal")
    public ResponseEntity<Map<String, Object>> getAuthorizedOverwithdrawal() {
        return ResponseEntity.ok(
            Map.of("Authorized overwithdrawal", bankAccount.getAuthorizedOverdraw())
        );
    }


    /**
     * Put request : putting money and updating balance
     * If the date is not specified, it's gonna be filled as today
     * @return JSON entity with the state of the process error/success
     */
    @PutMapping("/deposit")
    public ResponseEntity<Map<String, Object>> deposit(@RequestBody(required = true) Operation operation) {

        if(operation.getDate() == null) {
            operation.setDate(LocalDate.now());
        }

        try {
            bankAccount.deposit(operation.getDate(), operation.getAmount());
        } catch (IllegalArgumentException e) {
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
     * Put request : withdrawing money and updating balance
     * If the date is not specified, it's gonna be filled as today
     * @return JSON entity with the state of the process error/success
     */
    @PutMapping("/withdraw")
    public ResponseEntity<Map<String, Object>> withdraw(@RequestBody (required = true) Operation operation) {

        if(operation.getDate() == null) {
            operation.setDate(LocalDate.now());
        }

        try {
            bankAccount.withdraw(operation.getDate(), operation.getAmount(), operation.getLabel());
        } catch (IllegalArgumentException e) {
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
     * Put request : updating the overwithdrawal limit
     * @return JSON entity with the state of the process error/success
     */
    @PutMapping("/authorizedOverwithdrawal")
    public ResponseEntity<Map<String, Object>> changeAuthorizedWithdrawal(@RequestBody(required = true) float amount) {

        try {
            bankAccount.setAuthorizedOverdraw(amount);
        } catch (IllegalArgumentException e) {
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
