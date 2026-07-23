package org.example.bank.adapter.in.rest.controller;

import org.example.bank.adapter.in.rest.dto.CustomerResponse;
import org.example.bank.adapter.in.rest.dto.RegisterCustomerRequest;
import org.example.bank.application.service.BankService;
import org.example.bank.domain.model.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final BankService bankService;

    public CustomerController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> registerCustomer(
            @RequestBody RegisterCustomerRequest request
    ) {
        Customer customer = bankService.registerCustomer(
                request.name(),
                request.surname(),
                request.password()
        );

        CustomerResponse response = new CustomerResponse(
                customer.getAccId(),
                customer.getName(),
                customer.getSurname()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}
