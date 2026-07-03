package org.example;

import java.math.BigDecimal;

public class Account {

    int accID;

    BigDecimal balance;

    public Account(int accID, BigDecimal balance) {
        this.accID = accID;
        this.balance = balance;
    }

    boolean moneyDeposit(BigDecimal amount)
    {
        if (BigDecimal.ZERO.compareTo(amount) > 0) return false;

        this.balance = this.balance.add(amount);
        return true;
    }

    boolean moneyWithdraw(BigDecimal amount) {

        //amount balancedan kucuk ya da esit olmali
        if (amount.compareTo(this.balance) > 0) return false;

        //amount 0 dan buyuk olmali
        if (BigDecimal.ZERO.compareTo(amount) >= 0) return false;

        this.balance = this.balance.subtract(amount);
        return true;
    }

    boolean moneySend(BigDecimal amount) {

        if (amount.compareTo(this.balance) > 0) return false;

        if (BigDecimal.ZERO.compareTo(amount) >= 0) return false;

        this.balance = this.balance.subtract(amount);
        return true;
    }

    void moneyReceive(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }
}
