package com.bank.infrastructure.services;

import com.bank.adapters.dto.OperationDTO;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.time.LocalDate;

import com.bank.adapters.dto.MonthlyReviewDTO;
import com.bank.adapters.mappers.BankMapper;

import com.bank.infrastructure.entities.AccountEntity;
import com.bank.infrastructure.entities.OperationEntity;

import com.bank.domain.Operation;
import com.bank.domain.Account;
import com.bank.domain.MonthlyReview;

import com.bank.infrastructure.repositories.InfrastructureRepository;

import java.math.BigDecimal;

@Service
@Transactional
public class AccountServiceDatabase {

    private final InfrastructureRepository infrastructureRepository;
    private final BankMapper bankMapper;;

    public AccountServiceDatabase(InfrastructureRepository infrastructureRepository, BankMapper bankMapper) {
        this.infrastructureRepository = infrastructureRepository;
        this.bankMapper = bankMapper;
    }

    /**
     * Calling entityManager to get default account number
     * @return Account number
     */
    public String getAccountNumber() {
        return infrastructureRepository.findDefaultAccount().get().toDomain().getAccountNumber();
    }

    /**
     * Calling entityManager to get default balance
     * @return balance
     */
    public BigDecimal getBalance() {
        return infrastructureRepository.findDefaultAccount().get().toDomain().getBalance();
    }

    /**
     * Calling entityManager to get default authorized over withdrawal
     * @return over withdrawal
     */
    public BigDecimal getAuthorizedOverdraw() {
        return infrastructureRepository.findDefaultAccount().get().toDomain().getAuthorizedOverdraw();
    }

    /**
     * Calling entityManager to get default overdraw
     * @return overdraw
     */
    public BigDecimal getOverdraw() {
        return infrastructureRepository.findDefaultAccount().get().toDomain().getOverdraw();
    }

    /**
     * Calling entityManager to update the authorized overdraw
     */
    public void setAuthorizedOverdraw(BigDecimal authorizedOverdraw) {
        AccountEntity accountEntity = infrastructureRepository.findDefaultAccount().get();
        Account accountToUpdate = accountEntity.toDomain();

        accountToUpdate.setAuthorizedOverdraw(authorizedOverdraw);

        accountEntity = accountEntity.fromDomain(accountToUpdate);
        infrastructureRepository.save(accountEntity);
    }

    /**
     * Calling entityManager to make a deposit, save the account and a new operation
     */
    public BigDecimal deposit(LocalDate date, BigDecimal amount) throws IllegalArgumentException {
        AccountEntity accountEntity = infrastructureRepository.findDefaultAccount().get();
        Account accountToUpdate = accountEntity.toDomain();

        BigDecimal newBalance = accountToUpdate.deposit(date, amount);

        OperationEntity operation = new OperationEntity().fromDomain(new Operation(date, amount, "Deposit"));
        accountEntity = accountEntity.fromDomain(accountToUpdate);
        operation.setAccountEntity(accountEntity);
        accountEntity.addOperation(operation);
        infrastructureRepository.save(accountEntity);

        return newBalance;
    }

    /**
     * Calling entityManager to make a withdrawal, save the account and a new operation
     */
    public BigDecimal withdraw(LocalDate date, BigDecimal amount, String label) throws IllegalArgumentException {
        AccountEntity accountEntity = infrastructureRepository.findDefaultAccount().get();
        Account accountToUpdate = accountEntity.toDomain();

        BigDecimal newBalance = accountToUpdate.withdraw(date, amount, label);

        OperationEntity operation = new OperationEntity().fromDomain(new Operation(date, amount, label));
        accountEntity = accountEntity.fromDomain(accountToUpdate);
        operation.setAccountEntity(accountEntity);
        accountEntity.addOperation(operation);
        infrastructureRepository.save(accountEntity);

        return newBalance;
    }

    /**
     * Calling entityManager to get operations within a month and form a MonthlyReviewDTO
     * @return MonthlyReviewDTO
     */
    public MonthlyReviewDTO getMonthlyOperations() {
        List<OperationDTO> operationsThisMonth = infrastructureRepository.findOperationsByMonthAndYear().stream()
         .map(opEntity -> {
             return bankMapper.toOperationDTO(opEntity.toDomain());
         })
         .toList();
        Account account = infrastructureRepository.findDefaultAccount().get().toDomain();

        return new MonthlyReviewDTO(operationsThisMonth,
                                                       account.getBalance(),
                                                       account.getAccountType());
    }
}
