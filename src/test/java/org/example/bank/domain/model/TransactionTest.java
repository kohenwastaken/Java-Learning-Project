package org.example.bank.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    @Test
    void constructor_shouldCreateTransactionWithGivenValues() {
        Transaction transaction = new Transaction(
                1,
                Transaction.TransactionType.TRANSFER,
                BigDecimal.valueOf(1000),
                1,
                2
        );

        assertEquals(1, transaction.getTransactionId());
        assertEquals(Transaction.TransactionType.TRANSFER, transaction.getType());
        assertBigDecimalEquals(BigDecimal.valueOf(1000), transaction.getAmount());
        assertEquals(1, transaction.getSourceId());
        assertEquals(2, transaction.getTargetId());
    }

    @Test
    void constructor_whenTargetIdIsNull_shouldAllowNullTargetId() {
        Transaction transaction = new Transaction(
                1,
                Transaction.TransactionType.DEPOSIT,
                BigDecimal.valueOf(1000),
                1,
                null
        );
        assertNull(transaction.getTargetId());
    }

    private void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual) {
        assertEquals(0, expected.compareTo(actual));
    }
}
