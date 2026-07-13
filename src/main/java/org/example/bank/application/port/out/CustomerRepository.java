package org.example.bank.application.port.out;

import org.example.bank.domain.model.Customer;

import java.util.Optional;

public interface CustomerRepository {

    CustomerRepository save(Customer customer);

    Optional<Customer> findById(int customerId);

}
