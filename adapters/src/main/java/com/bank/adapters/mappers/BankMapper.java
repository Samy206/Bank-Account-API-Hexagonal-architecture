package com.bank.adapters.mappers;

import org.mapstruct.Mapper;

import com.bank.adapters.dto.AccountDTO;
import com.bank.adapters.dto.OperationDTO;
import com.bank.adapters.dto.MonthlyReviewDTO;

import com.bank.domain.Account;
import com.bank.domain.MonthlyReview;
import com.bank.domain.Operation;


@Mapper(componentModel = "spring")
public interface BankMapper {

    /**
     * Convert Account to AccountDTO
     * @param account
     * @return AccountDTO
     */
    AccountDTO toAccountDTO(Account account);

    /**
     * Convert AccountDTO to Account
     * @param accountDTO
     * @return Account
     */
    Account toAccount(AccountDTO accountDTO);


    /**
     * Convert OperationDTO to Operation
     * @param operationDTO
     * @return Operation
     */
    Operation toOperation(OperationDTO operationDTO);

    /**
     * Convert Operation to OperationDTO
     * @param operation
     * @return OperationDTO
     */
    OperationDTO toOperationDTO(Operation operation);


    /**
     * Convert MonthlyReview to MonthlyReviewDTO
     * @param monthlyReview
     * @return MonthlyReviewDTO
     */
    MonthlyReviewDTO toMonthlyReviewDTO(MonthlyReview monthlyReview);

        /**
     * Convert MonthlyReview to MonthlyReviewDTO
     * @param monthlyReview
     * @return MonthlyReviewDTO
     */
    MonthlyReview toMonthlyReview(MonthlyReviewDTO monthlyReviewDTO);

}