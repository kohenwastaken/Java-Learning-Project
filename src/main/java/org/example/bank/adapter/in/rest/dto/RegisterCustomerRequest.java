package org.example.bank.adapter.in.rest.dto;

public record RegisterCustomerRequest (
        String name,
        String surname,
        String password
){

}
