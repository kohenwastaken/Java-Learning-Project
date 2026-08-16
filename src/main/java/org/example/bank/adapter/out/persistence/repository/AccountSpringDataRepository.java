package org.example.bank.adapter.out.persistence.repository;

import org.example.bank.adapter.out.persistence.entity.AccountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountSpringDataRepository
        extends JpaRepository<AccountJpaEntity, Integer> {
}