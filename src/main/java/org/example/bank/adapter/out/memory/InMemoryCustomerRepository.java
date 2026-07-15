package org.example.bank.adapter.out.memory;

import org.example.bank.application.port.out.CustomerRepository;
import org.example.bank.domain.model.Customer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<Integer, Customer> customers = new HashMap<>();

    @Override
    public Customer save(Customer customer) {
        customers.put(customer.getAccId(), customer);
        return customer;
    }

    @Override
    public Optional<Customer> findById(int customerId) {
        return Optional.ofNullable(customers.get(customerId));
    }
}