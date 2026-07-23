package org.example.bank.adapter.in.rest.controller;

import org.example.bank.adapter.in.rest.dto.*;
import org.example.bank.application.service.BankService;
import org.example.bank.domain.model.Transaction;
import org.example.bank.domain.result.DepositResult;
import org.example.bank.domain.result.TransferResult;
import org.example.bank.domain.result.WithdrawResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final BankService bankService;

    public AccountController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping("/{accountId}/balance")
    public BalanceResponse showBalance(
            @PathVariable int accountId
    ) {
        BigDecimal balance = bankService.showBalance(accountId);

        return new BalanceResponse(accountId, balance);
    }

    @PostMapping("/{accountId}/deposits")
    public ResponseEntity<BalanceResponse> deposit(
            @PathVariable int accountId,         //comes from URL
            @RequestBody DepositRequest request  //comes from JSON Body
    ) {
        DepositResult result = bankService.depositToAccount(accountId, request.amount());

        if (result == DepositResult.INVALID_AMOUNT) {
            return  ResponseEntity.badRequest().build();
        }

        BigDecimal updatedBalance = bankService.showBalance(accountId);

        BalanceResponse response = new BalanceResponse(accountId, updatedBalance);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{accountId}/withdrawals")
    public ResponseEntity<BalanceResponse> withdraw(
            @PathVariable int accountId,
            @RequestBody WithdrawRequest request
    ) {
        WithdrawResult result = bankService.withdrawFromAccount(accountId, request.amount());

        if (result == WithdrawResult.INVALID_AMOUNT) {
            return ResponseEntity.badRequest().build();
        }

        if (result == WithdrawResult.INSUFFICIENT_BALANCE) {
            return ResponseEntity.badRequest().build();
        }

        BigDecimal updatedBalance = bankService.showBalance(accountId);

        BalanceResponse response = new BalanceResponse(accountId, updatedBalance);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{accountId}/transfers")
    public ResponseEntity<BalanceResponse> transfer(
            @PathVariable int accountId,
            @RequestBody TransferRequest request
    ) {
        TransferResult result = bankService.transferFromAccount(accountId, request.targetId(), request.amount());

        if (result == TransferResult.INVALID_AMOUNT) {
            return ResponseEntity.badRequest().build();
        }

        if (result == TransferResult.ACCOUNT_NOT_FOUND) {
            return ResponseEntity.badRequest().build();
        }

        if (result == TransferResult.INVALID_SELF_ID) {
            return ResponseEntity.badRequest().build();
        }

        if (result == TransferResult.INSUFFICIENT_BALANCE) {
            return ResponseEntity.badRequest().build();
        }

        BigDecimal updatedBalance = bankService.showBalance(accountId);

        BalanceResponse response = new BalanceResponse(accountId, updatedBalance);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}/transactions")
    public List<TransactionResponse> getTransactions(
            @PathVariable int accountId
    ) {
        List<Transaction> transactions = bankService.getTransactionsForAccount(accountId);

        List<TransactionResponse> responses = new ArrayList<>();

        for (Transaction transaction : transactions) {
            TransactionResponse response = new TransactionResponse(
                    transaction.getTransactionId(),
                    transaction.getType(),
                    transaction.getAmount(),
                    transaction.getSourceId(),
                    transaction.getTargetId()
            );
            responses.add(response);
        }

        return responses;
    }
}
