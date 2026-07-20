package org.example.bank.adapter.in.rest.dto;

public record CustomerResponse (
        int accountId,
        String name,
        String surname
) {
}
