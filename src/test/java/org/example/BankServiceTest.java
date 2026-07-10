package org.example;

import org.junit.jupiter.api.Test;


import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BankServiceTest {

    @Test
    void registerCustomer_shouldCreateCustomerWithInitialBalance() {
        //arrange
        BankService bankService = new BankService();

        //act
        Customer customer = bankService.registerCustomer("Arif", "Yılmaz", "123");

        //assert
        assertNotNull(customer);
        assertEquals(1, customer.getAccId());
        assertEquals("Arif", customer.getName());
        assertEquals("Yılmaz", customer.getSurname());
        assertBigDecimalEquals(BigDecimal.valueOf(1000), bankService.showBalance(customer.getAccId()));
    }

    @Test
    void registerCustomer_whenCalledMultipleTimes_shouldIncreaseCustomerId() {
        //arrange
        BankService bankService = new BankService();

        //act
        Customer customer = bankService.registerCustomer("Arif", "Yılmaz", "123");
        Customer customer1 = bankService.registerCustomer("Murat", "Kara", "123");

        //assert
        assertEquals(1, customer.getAccId());
        assertEquals(2, customer1.getAccId());
    }

    @Test
    void loginCustomer_ifPasswordIsCorrect_shouldReturnCustomer() {
        //arrange
        BankService bankService = new BankService();
        Customer customer = bankService.registerCustomer("Arif", "Yılmaz", "123");

        //act
        Customer loginResult = bankService.loginAccount(customer.getAccId(), "123");

        //assert
        assertNotNull(loginResult);
        assertEquals(customer.getAccId(), loginResult.getAccId());
        assertEquals("Arif", loginResult.getName());
        assertEquals("Yılmaz", loginResult.getSurname());
    }

    @Test
    void loginCustomer_ifPasswordIsWrong_shouldReturnNull() {
        //arrange
        BankService bankService = new BankService();
        Customer customer = bankService.registerCustomer("Arif", "Yılmaz", "123");

        //act
        Customer loginResult = bankService.loginAccount(customer.getAccId(), "120+3");

        //assert
        assertNull(loginResult);
    }

    @Test
    void loginCustomer_whenUserIdDoesNotExist_shouldReturnNull() {
        //arrange
        BankService bankService = new BankService();
        Customer customer = bankService.registerCustomer("Arif", "Yılmaz", "123");

        //act
        Customer loginResult = bankService.loginAccount(123, "123");

        //assert
        assertNull(loginResult);
    }

    @Test
    void depositToAccount_whenAmountIsPositive_shouldIncreaseBalanceAndCreateTransaction() {
        //arrange
        BankService bankService = new BankService();
        Customer customer = bankService.registerCustomer("Arif", "Yılmaz", "123");
        BigDecimal balanceBefore = bankService.showBalance(customer.getAccId());
        BigDecimal depositAmount = BigDecimal.valueOf(1000);

        //act
        DepositResult result = bankService.depositToAccount(customer.getAccId(), depositAmount);

        List<Transaction> transactions = bankService.getTransactionsForAccount(customer.getAccId());
        Transaction transaction = transactions.getFirst();

        //assert
        assertEquals(DepositResult.SUCCESS, result);
        assertBigDecimalEquals(balanceBefore.add(depositAmount), bankService.showBalance(customer.getAccId()));

        assertEquals(1, transactions.size());

        assertEquals(Transaction.TransactionType.DEPOSIT, transaction.getType());
        assertBigDecimalEquals(depositAmount, transaction.getAmount());
        assertEquals(customer.getAccId(), transaction.getSourceID());
        assertNull(transaction.getTargetID());
    }

    @Test
    void depositToAccount_whenAmountIsNegative_shouldFailAndKeepBalanceSameAndShouldNotCreateTransaction() {
        BankService bankService = new BankService();
        Customer customer = bankService.registerCustomer("Arif", "Yılmaz", "123");
        BigDecimal balanceBefore = bankService.showBalance(customer.getAccId());

        DepositResult result = bankService.depositToAccount(customer.getAccId(), BigDecimal.valueOf(-1000));

        List<Transaction> transactions = bankService.getTransactionsForAccount(customer.getAccId());

        assertEquals(DepositResult.INVALID_AMOUNT, result);
        assertBigDecimalEquals(balanceBefore, bankService.showBalance(customer.getAccId()));

        assertEquals(0, transactions.size());
    }

    @Test
    void depositToAccount_whenAmountIsZero_shouldFailAndKeepBalanceSameAndShouldNotCreateTransaction() {
        BankService bankService = new BankService();
        Customer customer = bankService.registerCustomer("Arif", "Yılmaz", "123");
        BigDecimal balanceBefore = bankService.showBalance(customer.getAccId());

        DepositResult result = bankService.depositToAccount(customer.getAccId(), BigDecimal.ZERO);

        List<Transaction> transactions = bankService.getTransactionsForAccount(customer.getAccId());

        assertEquals(DepositResult.INVALID_AMOUNT, result);
        assertBigDecimalEquals(balanceBefore, bankService.showBalance(customer.getAccId()));

        assertEquals(0, transactions.size());
    }

    @Test
    void withdrawFromAccount_whenBalanceIsSufficient_shouldDecreaseBalanceAndCreateTransaction() {
        BankService bankService = new BankService();
        Customer customer = bankService.registerCustomer("Arif", "Yılmaz", "123");
        BigDecimal balanceBefore = bankService.showBalance(customer.getAccId());
        BigDecimal withdrawAmount = BigDecimal.valueOf(1000);

        WithdrawResult result = bankService.withdrawFromAccount(customer.getAccId(), withdrawAmount);

        List<Transaction> transactions = bankService.getTransactionsForAccount(customer.getAccId());
        Transaction transaction = transactions.getFirst();

        assertEquals(WithdrawResult.SUCCESS, result);
        assertBigDecimalEquals(balanceBefore.subtract(withdrawAmount), bankService.showBalance(customer.getAccId()));

        assertEquals(1, transactions.size());

        assertEquals(Transaction.TransactionType.WITHDRAWAL, transaction.getType());
        assertBigDecimalEquals(withdrawAmount, transaction.getAmount());
        assertEquals(customer.getAccId(), transaction.getSourceID());
        assertNull(transaction.getTargetID());
    }

    @Test
    void withdrawFromAccount_whenBalanceIsInsufficient_shouldNotDecreaseBalanceAndShouldNotCreateTransaction() {
        BankService bankService = new BankService();
        Customer customer = bankService.registerCustomer("Arif", "Yılmaz", "123");
        BigDecimal balanceBefore = bankService.showBalance(customer.getAccId());

        WithdrawResult result = bankService.withdrawFromAccount(customer.getAccId(), BigDecimal.valueOf(2000));

        List<Transaction> transactions = bankService.getTransactionsForAccount(customer.getAccId());

        assertEquals(WithdrawResult.INSUFFICIENT_BALANCE, result);
        assertBigDecimalEquals(balanceBefore, bankService.showBalance(customer.getAccId()));

        assertEquals(0, transactions.size());
    }

    @Test
    void withdrawFromAccount_whenAmountIsNegative_shouldFailAndShouldNotCreateTransaction() {
        BankService bankService = new BankService();
        Customer customer = bankService.registerCustomer("Arif", "Yılmaz", "123");
        BigDecimal balanceBefore = bankService.showBalance(customer.getAccId());

        WithdrawResult result = bankService.withdrawFromAccount(customer.getAccId(), BigDecimal.valueOf(-1000));

        List<Transaction> transactions = bankService.getTransactionsForAccount(customer.getAccId());

        assertEquals(WithdrawResult.INVALID_AMOUNT, result);
        assertBigDecimalEquals(balanceBefore, bankService.showBalance(customer.getAccId()));

        assertEquals(0, transactions.size());
    }

    @Test
    void withdrawFromAccount_whenAmountIsZero_shouldFailAndShouldNotCreateTransaction() {
        BankService bankService = new BankService();
        Customer customer = bankService.registerCustomer("Arif", "Yılmaz", "123");
        BigDecimal balanceBefore = bankService.showBalance(customer.getAccId());

        WithdrawResult result = bankService.withdrawFromAccount(customer.getAccId(), BigDecimal.ZERO);

        List<Transaction> transactions = bankService.getTransactionsForAccount(customer.getAccId());

        assertEquals(WithdrawResult.INVALID_AMOUNT, result);
        assertBigDecimalEquals(balanceBefore, bankService.showBalance(customer.getAccId()));

        assertEquals(0, transactions.size());
    }

    @Test
    void transferFromAccount_whenMoneySendIsSuccessful_shouldTransferBalancesAndCreateTransaction() {
        BankService bankService = new BankService();
        Customer customer = bankService.registerCustomer("Arif", "Yılmaz", "123");
        Customer customer1 = bankService.registerCustomer("Arif", "Kara", "123");
        BigDecimal balanceBefore = bankService.showBalance(customer.getAccId());
        BigDecimal balanceBefore1 = bankService.showBalance(customer1.getAccId());
        BigDecimal sendAmount = BigDecimal.valueOf(500);

        TransferResult result = bankService.transferFromAccount(customer.getAccId(), customer1.getAccId(), sendAmount);

        List<Transaction> transactions = bankService.getTransactionsForAccount(customer.getAccId());
        Transaction transaction = transactions.getFirst();
        List<Transaction> receiverTransactions = bankService.getTransactionsForAccount(customer1.getAccId());
        Transaction receiverTransaction = receiverTransactions.getFirst();

        assertEquals(TransferResult.SUCCESS, result);
        assertBigDecimalEquals(balanceBefore.subtract(sendAmount), bankService.showBalance(customer.getAccId()));
        assertBigDecimalEquals(balanceBefore1.add(sendAmount), bankService.showBalance(customer1.getAccId()));

        assertEquals(1, transactions.size());
        assertEquals(Transaction.TransactionType.TRANSFER, transaction.getType());
        assertBigDecimalEquals(sendAmount, transaction.getAmount());
        assertEquals(customer.getAccId(), transaction.getSourceID());
        assertEquals(customer1.getAccId(), transaction.getTargetID());

        assertEquals(1, receiverTransactions.size());
        assertEquals(transaction.getTransactionID(), receiverTransaction.getTransactionID());
        assertEquals(Transaction.TransactionType.TRANSFER, receiverTransaction.getType());
        assertBigDecimalEquals(sendAmount, receiverTransaction.getAmount());
        assertEquals(customer.getAccId(), receiverTransaction.getSourceID());
        assertEquals(customer1.getAccId(), receiverTransaction.getTargetID());
    }

    @Test
    void transferFromAccount_whenReceiverAccountIsNull_shouldFailAndKeepBalancesSameAndNotCreateTransaction() {
        BankService bankService = new BankService();
        Customer customer = bankService.registerCustomer("Arif", "Yılmaz", "123");
        BigDecimal balanceBefore = bankService.showBalance(customer.getAccId());

        TransferResult result = bankService.transferFromAccount(customer.getAccId(), 10, BigDecimal.valueOf(500));

        List<Transaction> transactions = bankService.getTransactionsForAccount(customer.getAccId());

        assertEquals(TransferResult.ACCOUNT_NOT_FOUND, result);
        assertBigDecimalEquals(balanceBefore, bankService.showBalance(customer.getAccId()));

        assertEquals(0, transactions.size());
    }

    @Test
    void transferFromAccount_whenReceiverAccountIsSelf_shouldFailAndKeepBalanceSameAndNotCreateTransaction() {
        BankService bankService = new BankService();
        Customer customer = bankService.registerCustomer("Arif", "Yılmaz", "123");
        BigDecimal balanceBefore = bankService.showBalance(customer.getAccId());

        TransferResult result = bankService.transferFromAccount(customer.getAccId(), customer.getAccId(), BigDecimal.valueOf(500));

        List<Transaction> transactions = bankService.getTransactionsForAccount(customer.getAccId());

        assertEquals(TransferResult.INVALID_SELF_ID, result);
        assertBigDecimalEquals(balanceBefore, bankService.showBalance(customer.getAccId()));

        assertEquals(0, transactions.size());
    }

    @Test
    void transferFromAccount_whenAmountIsNegative_shouldReturnInvalidAmountAndNotCreateTransaction() {
        BankService bankService = new BankService();
        Customer customer = bankService.registerCustomer("Arif", "Yılmaz", "123");
        Customer customer1 = bankService.registerCustomer("Arif", "Ak", "123");
        BigDecimal balanceBefore = bankService.showBalance(customer.getAccId());
        BigDecimal balanceBefore1 = bankService.showBalance(customer1.getAccId());

        TransferResult result = bankService.transferFromAccount(customer.getAccId(), customer1.getAccId(), BigDecimal.valueOf(-1000));
        List<Transaction> transactions = bankService.getTransactionsForAccount(customer.getAccId());

        assertEquals(TransferResult.INVALID_AMOUNT, result);
        assertBigDecimalEquals(balanceBefore, bankService.showBalance(customer.getAccId()));
        assertBigDecimalEquals(balanceBefore1, bankService.showBalance(customer1.getAccId()));

        assertEquals(0, transactions.size());
    }

    @Test
    void transferFromAccount_whenAmountIsZero_shouldReturnInvalidAmountAndNotCreateTransaction() {
        BankService bankService = new BankService();
        Customer customer = bankService.registerCustomer("Arif", "Yılmaz", "123");
        Customer customer1 = bankService.registerCustomer("Arif", "Kara", "123");
        BigDecimal balanceBefore = bankService.showBalance(customer.getAccId());
        BigDecimal balanceBefore1 = bankService.showBalance(customer1.getAccId());

        TransferResult result = bankService.transferFromAccount(customer.getAccId(), customer1.getAccId(), BigDecimal.ZERO);

        List<Transaction> transactions = bankService.getTransactionsForAccount(customer.getAccId());

        assertEquals(TransferResult.INVALID_AMOUNT, result);
        assertBigDecimalEquals(balanceBefore, bankService.showBalance(customer.getAccId()));
        assertBigDecimalEquals(balanceBefore1, bankService.showBalance(customer1.getAccId()));

        assertEquals(0, transactions.size());
    }

    @Test
    void transferFromAccount_whenBalanceIsInsufficient_shouldReturnInsufficientBalanceAndNotCreateTransaction() {
        BankService bankService = new BankService();
        Customer customer = bankService.registerCustomer("Arif", "Yılmaz", "123");
        Customer customer1 = bankService.registerCustomer("Arif", "Ada", "123");
        BigDecimal balanceBefore = bankService.showBalance(customer.getAccId());
        BigDecimal balanceBefore1 = bankService.showBalance(customer.getAccId());

        TransferResult result = bankService.transferFromAccount(customer.getAccId(), customer1.getAccId(), BigDecimal.valueOf(99999));

        List<Transaction> transactions = bankService.getTransactionsForAccount(customer.getAccId());

        assertEquals(TransferResult.INSUFFICIENT_BALANCE, result);
        assertBigDecimalEquals(balanceBefore, bankService.showBalance(customer.getAccId()));
        assertBigDecimalEquals(balanceBefore1, bankService.showBalance(customer1.getAccId()));

        assertEquals(0, transactions.size());
    }

    private void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual) {
        assertEquals(0, expected.compareTo(actual));
    }

}
