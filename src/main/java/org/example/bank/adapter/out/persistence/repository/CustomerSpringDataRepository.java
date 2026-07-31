package org.example.bank.adapter.out.persistence.repository;

import org.example.bank.adapter.out.persistence.entity.CustomerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerSpringDataRepository extends JpaRepository<CustomerJpaEntity, Integer> {
}
