package com.bank.infrastructure.services;

import org.springframework.stereotype.Service;
import com.bank.domain.Operation;
import com.bank.domain.Account;
import com.bank.infrastructure.repositories.OperationRepository;

@Service
public class OperationServiceDatabase {

    private final OperationRepository operationRepository;

    public OperationServiceDatabase(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public List<Operation> findAllByAccount(Account account) {
        return operationRepository.findAllByAccount(account);
    }


}
