package org.example.bank.adapter.out.memory;

import org.example.bank.application.port.out.CustomerRepository;
import org.example.bank.domain.model.Customer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<Integer, Customer> customers = new HashMap<>();
    private int nextCustomerId = 1;

    @Override
    public Customer create(
            String name,
            String surname,
            String password
    ) {
        Customer customer = new Customer(
                name,
                surname,
                password,
                nextCustomerId++
        );

        customers.put(customer.getAccId(), customer);

        return customer;
    }

    @Override
    public Customer save(Customer customer) {
        customers.put(customer.getAccId(), customer);

        if (customer.getAccId() >= nextCustomerId) {
            nextCustomerId = customer.getAccId() + 1;
        }

        return customer;
    }

    @Override
    public Optional<Customer> findById(int customerId) {
        return Optional.ofNullable(customers.get(customerId));
    }
}