package com.bank.infrastructure.services;

import org.springframework.stereotype.Service;
import com.bank.domain.Operation;
import com.bank.domain.Account;
import com.bank.infrastructure.repositories.AccountRepository;

@Service
public class AccountServiceDatabase {

    private final AccountRepository accountRepository;

    public AccountServiceDatabase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }



}
