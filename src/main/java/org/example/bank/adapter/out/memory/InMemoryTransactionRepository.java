package org.example.bank.adapter.out.memory;

import org.example.bank.application.port.out.TransactionRepository;
import org.example.bank.domain.model.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTransactionRepository implements TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();
    private int nextTransactionId = 1;

    @Override
    public Transaction create(
            Transaction.TransactionType type,
            BigDecimal amount,
            int sourceId,
            Integer targetId
    ) {
        Transaction transaction = new Transaction(
                nextTransactionId++,
                type,
                amount,
                sourceId,
                targetId
        );

        transactions.add(transaction);

        return transaction;
    }

    @Override
    public Transaction save(Transaction transaction) {
        transactions.add(transaction);

        if (transaction.getTransactionId() >= nextTransactionId) {
            nextTransactionId = transaction.getTransactionId() + 1;
        }

        return transaction;
    }

    @Override
    public List<Transaction> findByAccountId(int accountId) {
        List<Transaction> result = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getSourceId() == accountId ||
                    (transaction.getTargetId() != null &&
                            transaction.getTargetId() == accountId)) {
                result.add(transaction);
            }
        }

        return result;
    }
}