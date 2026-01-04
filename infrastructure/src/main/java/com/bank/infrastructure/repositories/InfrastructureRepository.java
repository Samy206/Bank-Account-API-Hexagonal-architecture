package com.bank.infrastructure.repositories;

import java.util.List;

import com.bank.infrastructure.entities.AccountEntity;
import com.bank.infrastructure.entities.OperationEntity;
import java.util.Optional;

public interface InfrastructureRepository {

    Optional<AccountEntity> findDefaultAccount();
    AccountEntity save(AccountEntity accountEntity);
    void saveOperation(OperationEntity operationEntity);
    List<OperationEntity> findOperationsByMonthAndYear();
    
}
