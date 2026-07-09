package org.example;

import java.math.BigDecimal;

public class Account {

    private final int accID;

    private BigDecimal balance;

    public Account(int accID, BigDecimal balance) {
        this.accID = accID;
        this.balance = balance;
    }

    public int getAccID() {
        return this.accID;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    boolean moneyDeposit(BigDecimal amount)
    {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return false;

        this.balance = this.balance.add(amount);
        return true;
    }

    WithdrawResult moneyWithdraw(BigDecimal amount) {

        //amount 0 dan buyuk olmali
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return WithdrawResult.INVALID_AMOUNT;

        //amount balancedan kucuk ya da esit olmali
        if (amount.compareTo(this.balance) > 0) return WithdrawResult.INSUFFICIENT_BALANCE;

        this.balance = this.balance.subtract(amount);
        return WithdrawResult.SUCCESS;
    }

    TransferResult moneySend(BigDecimal amount) {

        if (BigDecimal.ZERO.compareTo(amount) >= 0) return TransferResult.INVALID_AMOUNT;

        if (amount.compareTo(this.balance) > 0) return TransferResult.INSUFFICIENT_BALANCE;

        this.balance = this.balance.subtract(amount);
        return TransferResult.SUCCESS;
    }

    boolean moneyReceive(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return false;

        this.balance = this.balance.add(amount);
        return true;
    }
}
