package org.example.bank.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.example.bank.domain.model.Transaction.TransactionType;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
public class TransactionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer transactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType type;

    @Column(
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal amount;

    @Column(name = "source_account_id", nullable = false)
    private Integer sourceAccountId;

    @Column(name = "target_account_id")
    private Integer targetAccountId;

    protected TransactionJpaEntity() {
    }

    public TransactionJpaEntity(
            Integer transactionId,
            TransactionType type,
            BigDecimal amount,
            Integer sourceAccountId,
            Integer targetAccountId
    ) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Integer getSourceAccountId() {
        return sourceAccountId;
    }

    public Integer getTargetAccountId() {
        return targetAccountId;
    }
}