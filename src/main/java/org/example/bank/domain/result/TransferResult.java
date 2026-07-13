package org.example.bank.domain.result;

public enum TransferResult{
    SUCCESS,
    INVALID_AMOUNT,
    INVALID_SELF_ID,
    INSUFFICIENT_BALANCE,
    ACCOUNT_NOT_FOUND
}