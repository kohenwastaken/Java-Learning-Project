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
            if(customer.accID == userID && password.equals(customer.pswrd))
            {
                return customer;
            }
        }
        return null;
    }

}
