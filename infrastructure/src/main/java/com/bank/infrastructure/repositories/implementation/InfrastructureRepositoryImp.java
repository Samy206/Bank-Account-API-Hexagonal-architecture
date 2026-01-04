package com.bank.infrastructure.repositories.implementation;


import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;


import com.bank.infrastructure.repositories.InfrastructureRepository;
import com.bank.infrastructure.entities.AccountEntity;
import com.bank.infrastructure.entities.OperationEntity;

@Repository
public class InfrastructureRepositoryImp implements InfrastructureRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AccountEntity> findDefaultAccount() {
        String jpql = "SELECT a FROM AccountEntity a";
        return Optional.ofNullable(entityManager.createQuery(jpql, AccountEntity.class).getSingleResult());
    }

    @Override
    public AccountEntity save(AccountEntity account) {
        return entityManager.merge(account);
    }

    @Override
    public void saveOperation(OperationEntity operationEntity) {
        entityManager.persist(operationEntity);
    }

    @Override
    public List<OperationEntity> findOperationsByMonthAndYear() {
        String jpql = "SELECT o FROM OperationEntity o";
        return entityManager.createQuery(jpql, OperationEntity.class)
                .getResultList()
                .stream()
                .filter(op -> op.getDate().getMonthValue() == java.time.LocalDate.now().getMonthValue()
                           && op.getDate().getYear() == java.time.LocalDate.now().getYear())
                .toList();
    }

}
