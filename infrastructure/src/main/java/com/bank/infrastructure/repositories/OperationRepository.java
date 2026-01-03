package com.bank.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bank.domain.Operation;

public interface OperationRepository extends JpaRepository<Operation, Long> {

}
