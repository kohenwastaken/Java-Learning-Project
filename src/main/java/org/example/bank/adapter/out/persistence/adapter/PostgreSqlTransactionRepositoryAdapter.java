package org.example.bank.adapter.out.persistence.adapter;

import org.example.bank.adapter.out.persistence.entity.TransactionJpaEntity;
import org.example.bank.adapter.out.persistence.repository.TransactionSpringDataRepository;
import org.example.bank.application.port.out.TransactionRepository;
import org.example.bank.domain.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class PostgreSqlTransactionRepositoryAdapter
        implements TransactionRepository {

    private final TransactionSpringDataRepository springDataRepository;

    public PostgreSqlTransactionRepositoryAdapter(
            TransactionSpringDataRepository springDataRepository
    ) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Transaction create(
            Transaction.TransactionType type,
            BigDecimal amount,
            int sourceId,
            Integer targetId
    ) {
        TransactionJpaEntity entity = new TransactionJpaEntity(
                null,
                type,
                amount,
                sourceId,
                targetId
        );

        TransactionJpaEntity savedEntity =
                springDataRepository.save(entity);

        return toDomain(savedEntity);
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionJpaEntity entity = toEntity(transaction);

        TransactionJpaEntity savedEntity =
                springDataRepository.save(entity);

        return toDomain(savedEntity);
    }

    @Override
    public List<Transaction> findByAccountId(int accountId) {
        return springDataRepository
                .findBySourceAccountIdOrTargetAccountIdOrderByTransactionIdAsc(
                        accountId,
                        accountId
                )
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private TransactionJpaEntity toEntity(Transaction transaction) {
        return new TransactionJpaEntity(
                transaction.getTransactionId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getSourceId(),
                transaction.getTargetId()
        );
    }

    private Transaction toDomain(TransactionJpaEntity entity) {
        return new Transaction(
                entity.getTransactionId(),
                entity.getType(),
                entity.getAmount(),
                entity.getSourceAccountId(),
                entity.getTargetAccountId()
        );
    }
}