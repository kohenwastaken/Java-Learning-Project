package org.example.bank.adapter.out.memory;

import org.example.bank.application.port.out.TransactionRepository;
import org.example.bank.domain.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTransactionRepository implements TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public Transaction save(Transaction transaction) {
        transactions.add(transaction);
        return  transaction;
    }

    @Override
    public List<Transaction> findByAccountId(int accountId) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getSourceId() == accountId ||
                    transaction.getTargetId() != null && transaction.getTargetId() == accountId) {
                result.add(transaction);
            }
        }
        return result;
    }
}
