package org.example.bank.application.port.out;

import org.example.bank.domain.model.Account;

import java.util.Optional;

public interface AccountRepository {

    Account save(Account account);

    Optional<Account> findById(int accountId);
}
