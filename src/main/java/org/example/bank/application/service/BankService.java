package org.example.bank.application.service;

import org.example.bank.application.port.out.AccountRepository;
import org.example.bank.application.port.out.CustomerRepository;
import org.example.bank.application.port.out.TransactionRepository;
import org.example.bank.domain.model.Account;
import org.example.bank.domain.model.Customer;
import org.example.bank.domain.model.Transaction;
import org.example.bank.domain.result.DepositResult;
import org.example.bank.domain.result.TransferResult;
import org.example.bank.domain.result.WithdrawResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


public class BankService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private int userId = 1;
    private int transactionId = 1;

    public BankService(
            CustomerRepository customerRepository,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository
    ) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Customer registerCustomer (String name, String surname, String password) {

        Customer customer = new Customer(name, surname, password, this.userId);
        customerRepository.save(customer);

        Account account = new Account(this.userId, BigDecimal.valueOf(1000));
        accountRepository.save(account);

        this.userId++;

        return customer;
    }

    public Customer loginAccount (int userId, String password) {
        return customerRepository.findById(userId)
                .filter(customer -> customer.passwordMatches(password))
                .orElse(null);
    }

    private Account findAccountById(int accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Hesap bulunamadi: " + accountId)
                );
    }

    public BigDecimal showBalance(int userId) {
        Account account = findAccountById(userId);
        return account.getBalance();
    }

    public DepositResult depositToAccount(int userId, BigDecimal amount) {

        Account account = findAccountById(userId);
        if (account.moneyDeposit(amount)) {
            accountRepository.save(account);

            Transaction x = new Transaction(
                    this.transactionId++,
                    Transaction.TransactionType.DEPOSIT,
                    amount,
                    account.getAccId(),
                    null
            );
            transactionRepository.save(x);

            return DepositResult.SUCCESS;
        }
        else return DepositResult.INVALID_AMOUNT;
    }

    public WithdrawResult withdrawFromAccount(int userId, BigDecimal amount) {

        Account account = findAccountById(userId);

        WithdrawResult result = account.moneyWithdraw(amount);

        if (result == WithdrawResult.SUCCESS) {
            accountRepository.save(account);

            Transaction y = new Transaction(
                    this.transactionId++,
                    Transaction.TransactionType.WITHDRAWAL,
                    amount,
                    account.getAccId(),
                    null
            );
            transactionRepository.save(y);
        }
        return result;
    }

    public TransferResult transferFromAccount(int userId, int targetId, BigDecimal amount){

        Account senderAccount = findAccountById(userId);
        Optional<Account> receiverOptional = accountRepository.findById(targetId);

        if (receiverOptional.isEmpty()) return TransferResult.ACCOUNT_NOT_FOUND;

        Account receiverAccount = receiverOptional.get();

        if (receiverAccount.getAccId() == senderAccount.getAccId()) return TransferResult.INVALID_SELF_ID;

        TransferResult result = senderAccount.moneySend(amount);

        if (result != TransferResult.SUCCESS) return result;

        boolean receiveResult = receiverAccount.moneyReceive(amount);

        if (!receiveResult) {
            throw new IllegalStateException("Transfer basarili ama alici hesaba gitmedi: " + amount);
        }

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        Transaction z = new Transaction(
                this.transactionId++,
                Transaction.TransactionType.TRANSFER,
                amount,
                senderAccount.getAccId(),
                receiverAccount.getAccId()
        );
        transactionRepository.save(z);

        return TransferResult.SUCCESS;
    }

    public List<Transaction> getTransactionsForAccount(int userId) {
        return transactionRepository.findByAccountId(userId);
    }

}
