package org.example.bank.adapter.in.rest.dto;

import org.example.bank.domain.model.Transaction;

import java.math.BigDecimal;

public record TransactionResponse(
        int transactionId,
        Transaction.TransactionType type,
        BigDecimal amount,
        int sourceId,
        Integer targetId
) {
}
