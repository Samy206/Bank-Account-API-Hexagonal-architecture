package com.bank.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bank.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
