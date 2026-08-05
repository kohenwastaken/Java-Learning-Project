package org.example.bank.adapter.out.persistence.repository;

import org.example.bank.adapter.out.persistence.entity.TransactionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionSpringDataRepository
        extends JpaRepository<TransactionJpaEntity, Integer> {

    List<TransactionJpaEntity>
    findBySourceAccountIdOrTargetAccountIdOrderByTransactionIdAsc(
            Integer sourceAccountId,
            Integer targetAccountId
    );
}