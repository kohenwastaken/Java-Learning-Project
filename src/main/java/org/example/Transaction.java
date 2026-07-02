package org.example;

import java.math.BigDecimal;

public class Transaction {

    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, SENT, RECEIVED
    }

    int transactionID;

    TransactionType type;

    BigDecimal amount;

    int sourceID;

    Integer targetID;

    public Transaction(
            int transactionID,
            TransactionType type,
            BigDecimal amount,
            int sourceID,
            Integer targetID
    )
    {
        this.transactionID = transactionID;
        this.type = type;
        this.amount = amount;
        this.sourceID = sourceID;
        this.targetID = targetID;
    }
}
