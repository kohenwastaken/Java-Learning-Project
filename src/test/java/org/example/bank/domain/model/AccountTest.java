package org.example.bank.domain.model;

import org.example.bank.domain.result.TransferResult;
import org.example.bank.domain.result.WithdrawResult;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void moneyDeposit_whenAmountIsPositive_shouldIncreaseBalance() {
        //arrange
        Account account = new Account(1, BigDecimal.valueOf(1000));

        //act
        boolean result = account.moneyDeposit(BigDecimal.valueOf(500));

        //assert
        assertTrue(result);
        assertBigDecimalEquals(BigDecimal.valueOf(1500), account.getBalance());
    }

    @Test
    void moneyDeposit_whenAmountIsZero_shouldFailAndKeepBalanceSame() {
        //arrange
        Account account = new Account(1, BigDecimal.valueOf(1000));

        //act
        boolean result = account.moneyDeposit(BigDecimal.ZERO);

        //assert
        assertFalse(result);
        assertBigDecimalEquals(BigDecimal.valueOf(1000), account.getBalance());
    }

    @Test
    void moneyDeposit_whenAmountIsNegative_shouldFailAndKeepBalanceSame() {
        //arrange
        Account account = new Account(1, BigDecimal.valueOf(1000));

        //act
        boolean result = account.moneyDeposit(BigDecimal.valueOf(-500));

        //assert
        assertFalse(result);
        assertBigDecimalEquals(BigDecimal.valueOf(1000), account.getBalance());
    }

    @Test
    void moneyWithdraw_whenBalanceIsEnough_shouldSucceedAndDecreaseBalance() {
        //arrange
        Account account = new Account(1, BigDecimal.valueOf(1000));

        //act
        WithdrawResult result = account.moneyWithdraw(BigDecimal.valueOf(1000));

        //assert
        assertEquals(WithdrawResult.SUCCESS, result);
        assertBigDecimalEquals(BigDecimal.valueOf(0), account.getBalance());
    }

    @Test
    void moneyWithdraw_whenAmountIsNegative_shouldReturnInvalidAmountAndKeepBalanceSame() {
        Account account = new Account(1, BigDecimal.valueOf(1000));

        WithdrawResult result = account.moneyWithdraw(BigDecimal.valueOf(-1000));

        assertEquals(WithdrawResult.INVALID_AMOUNT, result);
        assertBigDecimalEquals(BigDecimal.valueOf(1000), account.getBalance());
    }

    @Test
    void moneyWithdraw_whenAmountIsZero_shouldReturnInvalidAmountAndKeepBalanceSame() {
        Account account = new Account(1, BigDecimal.valueOf(1000));

        WithdrawResult result = account.moneyWithdraw(BigDecimal.valueOf(0));

        assertEquals(WithdrawResult.INVALID_AMOUNT, result);
        assertBigDecimalEquals(BigDecimal.valueOf(1000), account.getBalance());
    }

    @Test
    void moneyWithdraw_whenBalanceIsInsufficient_shouldReturnInsufficientBalanceAndKeepBalanceSame() {
        Account account = new Account(1, BigDecimal.valueOf(1000));

        WithdrawResult result = account.moneyWithdraw(BigDecimal.valueOf(1500));

        assertEquals(WithdrawResult.INSUFFICIENT_BALANCE, result);
        assertBigDecimalEquals(BigDecimal.valueOf(1000), account.getBalance());
    }

    @Test
    void moneySend_whenAmountIsValid_shouldDecreaseBalance() {
        Account account = new Account(1, BigDecimal.valueOf(1000));

        TransferResult result = account.moneySend(BigDecimal.valueOf(1000));

        assertEquals(TransferResult.SUCCESS, result);
        assertBigDecimalEquals(BigDecimal.valueOf(0), account.getBalance());
    }

    @Test
    void moneySend_whenAmountIsNegative_shouldReturnInvalidAmountAndKeepBalanceSame() {
        Account account = new Account(1, BigDecimal.valueOf(1000));

        TransferResult result = account.moneySend(BigDecimal.valueOf(-1000));

        assertEquals(TransferResult.INVALID_AMOUNT, result);
        assertBigDecimalEquals(BigDecimal.valueOf(1000), account.getBalance());
    }

    @Test
    void moneySend_whenAmountIsZero_shouldReturnInvalidAmountAndKeepBalanceSame() {
        Account account = new Account(1, BigDecimal.valueOf(1000));

        TransferResult result = account.moneySend(BigDecimal.ZERO);

        assertEquals(TransferResult.INVALID_AMOUNT, result);
        assertBigDecimalEquals(BigDecimal.valueOf(1000), account.getBalance());
    }

    @Test
    void moneySend_whenBalanceIsInsufficient_shouldReturnInsufficientBalanceAndKeepBalanceSame() {
        Account account = new Account(1, BigDecimal.valueOf(1000));

        TransferResult result = account.moneySend(BigDecimal.valueOf(1500));

        assertEquals(TransferResult.INSUFFICIENT_BALANCE, result);
        assertBigDecimalEquals(BigDecimal.valueOf(1000), account.getBalance());
    }

    @Test
    void moneyReceive_whenAmountIsPositive_shouldIncreaseBalance() {
        Account account = new Account(1, BigDecimal.valueOf(1000));

        boolean result = account.moneyReceive(BigDecimal.valueOf(1500));

        assertTrue(result);
        assertBigDecimalEquals(BigDecimal.valueOf(2500), account.getBalance());
    }

    @Test
    void moneyReceive_whenAmountIsZero_shouldFailAndKeepBalanceSame() {
        Account account = new Account(1, BigDecimal.valueOf(1000));

        boolean result = account.moneyReceive(BigDecimal.ZERO);

        assertFalse(result);
        assertBigDecimalEquals(BigDecimal.valueOf(1000), account.getBalance());
    }

    @Test
    void moneyReceive_whenAmountIsNegative_shouldFailAndKeepBalanceSame() {
        Account account = new Account(1, BigDecimal.valueOf(1000));

        boolean result = account.moneyReceive(BigDecimal.valueOf(-1000));

        assertFalse(result);
        assertBigDecimalEquals(BigDecimal.valueOf(1000), account.getBalance());
    }
    private void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual) {
        assertEquals(0, expected.compareTo(actual));
    }
}
