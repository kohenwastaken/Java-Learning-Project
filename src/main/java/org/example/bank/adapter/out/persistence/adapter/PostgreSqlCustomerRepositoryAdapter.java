package org.example.bank.adapter.out.persistence.adapter;

import org.example.bank.adapter.out.persistence.entity.CustomerJpaEntity;
import org.example.bank.adapter.out.persistence.repository.CustomerSpringDataRepository;
import org.example.bank.application.port.out.CustomerRepository;
import org.example.bank.domain.model.Customer;

import java.util.Optional;

public class PostgreSqlCustomerRepositoryAdapter implements CustomerRepository {

    private final CustomerSpringDataRepository springDataRepository;

    public PostgreSqlCustomerRepositoryAdapter(
            CustomerSpringDataRepository springDataRepository
    ) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Customer create(
            String name,
            String surname,
            String password
    ) {
        CustomerJpaEntity entity = new CustomerJpaEntity(
                null,
                name,
                surname,
                password
        );

        CustomerJpaEntity savedEntity =
                springDataRepository.save(entity);

        return toDomain(savedEntity);
    }

    @Override
    public Customer save(Customer customer) {
        CustomerJpaEntity entity = toEntity(customer);

        CustomerJpaEntity savedEntity =
                springDataRepository.save(entity);

        return toDomain(savedEntity);
    }

    @Override
    public Optional<Customer> findById(int customerId) {
        return springDataRepository
                .findById(customerId)
                .map(this::toDomain);
    }

    private CustomerJpaEntity toEntity(Customer customer) {
        return new CustomerJpaEntity(
                customer.getAccId(),
                customer.getName(),
                customer.getSurname(),
                customer.getPassword()
        );
    }

    private Customer toDomain(CustomerJpaEntity entity) {
        return new Customer(
                entity.getName(),
                entity.getSurname(),
                entity.getPassword(),
                entity.getAccountId()
        );
    }
}