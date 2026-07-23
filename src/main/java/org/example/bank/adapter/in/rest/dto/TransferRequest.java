package org.example.bank.adapter.in.rest.dto;

import java.math.BigDecimal;

public record TransferRequest(
        int targetId,
        BigDecimal amount
) {

}