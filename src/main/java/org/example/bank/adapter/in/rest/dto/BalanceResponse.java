package org.example.bank.adapter.in.rest.dto;

import java.math.BigDecimal;

public record BalanceResponse (
        int accountId,
        BigDecimal balance
) {
}
