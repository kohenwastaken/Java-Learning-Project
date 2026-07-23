package org.example.bank.adapter.in.rest.dto;

import java.math.BigDecimal;

public record WithdrawRequest (
        BigDecimal amount
) {

}
