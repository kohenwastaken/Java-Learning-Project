package org.example.bank.application.port.out;

import org.example.bank.domain.model.Transaction;

import java.util.List;

public interface TransactionRepository {

    Transaction save(Transaction transaction);

    List<Transaction> findByAccountId(int accountId);
}
