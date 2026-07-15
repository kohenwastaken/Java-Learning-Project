package org.example.bank.adapter.out.memory;

import org.example.bank.application.port.out.AccountRepository;
import org.example.bank.domain.model.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryAccountRepository implements AccountRepository {

    private final Map<Integer, Account> accounts = new HashMap<>();

    @Override
    public Account save(Account account) {
        accounts.put(account.getAccId(), account);
        return  account;
    }

    @Override
    public Optional<Account> findById(int accountId) {
        return Optional.ofNullable(accounts.get(accountId));
    }
}
