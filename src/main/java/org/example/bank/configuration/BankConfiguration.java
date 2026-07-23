package org.example.bank.configuration;

import org.example.bank.adapter.out.memory.InMemoryAccountRepository;
import org.example.bank.adapter.out.memory.InMemoryCustomerRepository;
import org.example.bank.adapter.out.memory.InMemoryTransactionRepository;
import org.example.bank.application.port.out.AccountRepository;
import org.example.bank.application.port.out.CustomerRepository;
import org.example.bank.application.port.out.TransactionRepository;
import org.example.bank.application.service.BankService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BankConfiguration {

    @Bean
    public CustomerRepository customerRepository() {
        return new InMemoryCustomerRepository();
    }

    @Bean
    public AccountRepository accountRepository() {
        return new InMemoryAccountRepository();
    }

    @Bean
    public TransactionRepository transactionRepository() {
        return new InMemoryTransactionRepository();
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