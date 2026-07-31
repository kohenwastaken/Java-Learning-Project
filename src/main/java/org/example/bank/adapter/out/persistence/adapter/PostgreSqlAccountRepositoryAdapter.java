package org.example.bank.adapter.out.persistence.adapter;

import org.example.bank.adapter.out.persistence.entity.AccountJpaEntity;
import org.example.bank.adapter.out.persistence.repository.AccountSpringDataRepository;
import org.example.bank.application.port.out.AccountRepository;
import org.example.bank.domain.model.Account;

import java.util.Optional;

public class PostgreSqlAccountRepositoryAdapter
        implements AccountRepository {

    private final AccountSpringDataRepository springDataRepository;

    public PostgreSqlAccountRepositoryAdapter(
            AccountSpringDataRepository springDataRepository
    ) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Account save(Account account) {
        AccountJpaEntity entity = toEntity(account);

        AccountJpaEntity savedEntity =
                springDataRepository.save(entity);

        return toDomain(savedEntity);
    }

    @Override
    public Optional<Account> findById(int accountId) {
        return springDataRepository
                .findById(accountId)
                .map(this::toDomain);
    }

    private AccountJpaEntity toEntity(Account account) {
        return new AccountJpaEntity(
                account.getAccId(),
                account.getBalance()
        );
    }

    private Account toDomain(AccountJpaEntity entity) {
        return new Account(
                entity.getAccountId(),
                entity.getBalance()
        );
    }
}