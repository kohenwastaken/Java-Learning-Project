package org.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BankService {

    private List<Customer> customerList = new ArrayList<>();

    private List<Account> accountList = new ArrayList<>();

    private  List<Transaction> transactionList = new ArrayList<>();

    private int userID = 1;

    private int transactionID = 1;

    Customer registerCustomer (String name, String surname, String password) {

        Customer customer = new Customer(name, surname, password, this.userID);
        this.customerList.add(customer);

        Account account = new Account(this.userID, new BigDecimal(1000));
        this.accountList.add(account);

        this.userID++;

        return customer;
    }

    Customer loginAccount (int userID, String password) {

        for (Customer customer : customerList){
            if(customer.getAccID() == userID && customer.passwordMatches(password))
            {
                return customer;
            }
        }
        return null;
    }

    private Account findAccountByID(int userID) {
        for (Account account : this.accountList) {
            if (account.getAccID() == userID) {
                return account;
            }
        }
        throw new IllegalArgumentException("Hesap bulunamadi: " + userID);
    }

    private Account searchAccountByID(int targetID) {
        for (Account account : this.accountList) {
            if (account.getAccID() == targetID)
                return account;
        }
        return null;
    }

    BigDecimal showBalance(int userID) {
        Account account = findAccountByID(userID);
        return account.getBalance();
    }

    DepositResult depositToAccount(int userID, BigDecimal amount) {

        Account account = findAccountByID(userID);
        if (account.moneyDeposit(amount)) {

            Transaction x = new Transaction(
                    this.transactionID++,
                    Transaction.TransactionType.DEPOSIT,
                    amount,
                    account.getAccID(),
                    null
            );
            this.transactionList.add(x);

            return DepositResult.SUCCESS;
        }
        else return DepositResult.INVALID_AMOUNT;
    }

    WithdrawResult withdrawFromAccount(int userID, BigDecimal amount) {

        Account account = findAccountByID(userID);

        WithdrawResult result = account.moneyWithdraw(amount);

        if (result == WithdrawResult.SUCCESS) {
            Transaction y = new Transaction(
                    this.transactionID++,
                    Transaction.TransactionType.WITHDRAWAL,
                    amount,
                    account.getAccID(),
                    null
            );
            this.transactionList.add(y);
        }
        return result;
    }

    TransferResult transferFromAccount(int userID, int targetID, BigDecimal amount){

        Account senderAccount = findAccountByID(userID);
        Account receiverAccount = searchAccountByID(targetID);
        if (receiverAccount == null) return TransferResult.ACCOUNT_NOT_FOUND;
        if (receiverAccount.getAccID() == senderAccount.getAccID()) return TransferResult.INVALID_SELF_ID;

        TransferResult result = senderAccount.moneySend(amount);
        if (result == TransferResult.SUCCESS) {
            receiverAccount.moneyReceive(amount);
            Transaction z = new Transaction(
                    this.transactionID++,
                    Transaction.TransactionType.TRANSFER,
                    amount,
                    senderAccount.getAccID(),
                    receiverAccount.getAccID()
            );
            this.transactionList.add(z);
        }
        return result;
    }

    List<Transaction> getTransactionsForAccount (int userID) {
        List<Transaction> result = new ArrayList<Transaction>();
        for (Transaction transaction : this.transactionList) {
            if (transaction.getSourceID() == userID ||
                    (transaction.getTargetID() != null) && transaction.getTargetID() == userID) {
                result.add(transaction);
            }
        }
        return result;
    }

}
