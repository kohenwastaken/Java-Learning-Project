package org.example.bank.configuration;

import org.example.bank.adapter.out.persistence.adapter.PostgreSqlAccountRepositoryAdapter;
import org.example.bank.adapter.out.persistence.adapter.PostgreSqlCustomerRepositoryAdapter;
import org.example.bank.adapter.out.persistence.adapter.PostgreSqlTransactionRepositoryAdapter;
import org.example.bank.adapter.out.persistence.repository.AccountSpringDataRepository;
import org.example.bank.adapter.out.persistence.repository.CustomerSpringDataRepository;
import org.example.bank.adapter.out.persistence.repository.TransactionSpringDataRepository;
import org.example.bank.application.port.out.AccountRepository;
import org.example.bank.application.port.out.CustomerRepository;
import org.example.bank.application.port.out.TransactionRepository;
import org.example.bank.application.service.BankService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BankConfiguration {

    @Bean
    public CustomerRepository customerRepository(
            CustomerSpringDataRepository springDataRepository
    ) {
        return new PostgreSqlCustomerRepositoryAdapter(
                springDataRepository
        );
    }

    @Bean
    public AccountRepository accountRepository(
            AccountSpringDataRepository springDataRepository
    ) {
        return new PostgreSqlAccountRepositoryAdapter(
                springDataRepository
        );
    }

    @Bean
    public TransactionRepository transactionRepository(
            TransactionSpringDataRepository springDataRepository
    ) {
        return new PostgreSqlTransactionRepositoryAdapter(
                springDataRepository
        );
    }

    @Bean
    public BankService bankService(
            CustomerRepository customerRepository,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository
    ) {
        return new BankService(
                customerRepository,
                accountRepository,
                transactionRepository
        );
    }
}