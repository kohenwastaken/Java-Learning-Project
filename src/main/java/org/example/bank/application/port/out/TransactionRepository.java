package org.example.bank.application.port.out;

import org.example.bank.domain.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository {

    Transaction create(
            Transaction.TransactionType type,
            BigDecimal amount,
            int sourceId,
            Integer targetId
    );

    Transaction save(Transaction transaction);

    List<Transaction> findByAccountId(int accountId);
}