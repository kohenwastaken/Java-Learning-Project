package org.example;

import java.math.BigDecimal;

public class Account {

    int accID;

    BigDecimal balance;

    public Account(int accID, BigDecimal balance) {
        this.accID = accID;
        this.balance = balance;
    }

    void addDeposit(BigDecimal amount)
    {
        this.balance = this.balance.add(amount);
    }

    void withdrawMoney(BigDecimal amount)
    {
        this.balance = this.balance.add(amount);
    }
}
