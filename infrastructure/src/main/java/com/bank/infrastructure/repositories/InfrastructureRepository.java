package com.bank.infrastructure.repositories;

import java.util.List;

import com.bank.infrastructure.entities.AccountEntity;
import com.bank.infrastructure.entities.OperationEntity;
import java.util.Optional;

public interface InfrastructureRepository {
    /**
     * Get the only account injected via liquibase
     * @return default account
     */
    Optional<AccountEntity> findDefaultAccount();

    /**
     * Update the default account in the database
     * @param accountEntity
     */
    void save(AccountEntity accountEntity);


    /**
     * Gets the operation within a month
     * @return list of  monthly operation
     */
    List<OperationEntity> findOperationsByMonthAndYear();
    
}
