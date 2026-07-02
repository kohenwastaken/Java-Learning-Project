package org.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {


    public static void main() {

        ArrayList<Customer> customerList = new ArrayList<Customer>();
        ArrayList<Account> accountList = new ArrayList<Account>();
        ArrayList<Transaction> transactionList = new ArrayList<Transaction>();

        int tID = 1;
        int ID = 1;
        int choice;

        Scanner sc = new Scanner(System.in);

        while (true){
            IO.println("Banka sistemine hosgeldiniz." +
                    "\n Secenekleriniz asagidaki gibidir:" +
                    "\n 1. Kayit Ol" +
                    "\n 2. Giris yap" +
                    "\n 3. Cikis yap");
                    choice = sc.nextInt();
                    sc.nextLine();
            switch (choice){
                case 1:
                    Customer registeredCustomer = registry(sc, ID++);
                    customerList.add(registeredCustomer);

                    Account registeredAccount = new Account(registeredCustomer.accID, new BigDecimal("1000"));
                    accountList.add(registeredAccount);

                    IO.println("Hesap olusturuldu");
                    break;
                case 2:
                    Customer loggedInCustomer = login(sc, customerList);
                    if (loggedInCustomer == null) break;

                    //giris sonrasi islemler
                    accountMenu(sc, loggedInCustomer, accountList, transactionList, tID);

                    break;
                case 3:
                    IO.println("uygulama kapatiliyor..");
                    return;
            }
        }
    }

    static Customer registry(Scanner sc, int accID) {
        IO.println("Ad Soyad giriniz..");
        String name = sc.nextLine();

        IO.println("Sifre Olusturunuz..");
        String pswrd = sc.nextLine();

        name = name.trim();
        int index = name.lastIndexOf(" ");

        String firstName = name.substring(0, index);
        String lastName = name.substring(index + 1);

        return new Customer(firstName, lastName, pswrd, accID);
    }

    static Customer login(Scanner sc, List<Customer> customerList){

        IO.println("Hesap ID giriniz..");
        int accID = sc.nextInt();
        sc.nextLine();

        Customer requestedCustomer = null;

        for (Customer customer1 : customerList){
            if(customer1.accID == accID)
            {
                requestedCustomer = customer1;
                break;
            }
        }

        if (requestedCustomer == null)
        {
            IO.println("Belirtilen ID yoktur. Sisteme geri donuluyor..");
            return null;
        }

        IO.println("Sifre giriniz..");
        String enteredPswrd = sc.nextLine();
        if (enteredPswrd.equals(requestedCustomer.pswrd))
        {
            return requestedCustomer;
        }
        else{
            IO.println("Sifre yanlis. Sisteme geri donuluyor..");
            return null;
        }

    }

    static Account findAffiliatedAccount(Customer loggedInCustomer, List<Account> accountList)
    {
        Account requestedAccount = null;
        for (Account account1 : accountList)
        {
            if(account1.accID == loggedInCustomer.accID)
            {
                requestedAccount = account1;
            }
        }
        return requestedAccount;
    }

    static void accountMenu(Scanner sc, Customer loggedInCustomer, List<Account> accountList, List<Transaction> transactionList, int tID)
    {
        Account loggedInAccount = findAffiliatedAccount(loggedInCustomer, accountList);
        while (true)
        {
            IO.println("Hosgeldiniz " + loggedInCustomer.name + " " + loggedInCustomer.surname + " !" +
                    "\n Secenekleriniz asagidaki gibidir:" +
                    "\n 1. Para yatir" +
                    "\n 2. Para cek" +
                    "\n 3. Para gonder" +
                    "\n 4. Hesap hareketleri" +
                    "\n 5. Hesaptan cikis");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice){
                case 1:
                    IO.println("yatirmak istediginiz miktari giriniz.." +
                            "\n mevcut bakiye: " + loggedInAccount.balance);

                    BigDecimal depositAmount = new BigDecimal(sc.nextLine());

                    loggedInAccount.addDeposit(depositAmount);

                    Transaction x = new Transaction(
                            tID++,
                            Transaction.TransactionType.DEPOSIT,
                            depositAmount,
                            loggedInAccount.accID,
                            null
                    );
                    transactionList.add(x);

                    IO.println("Guncel bakiye: " + loggedInAccount.balance);
                    break;
                case 2:
                    IO.println("cekmek istediginiz miktari giriniz.." +
                            "\n mevcut bakiye: " + loggedInAccount.balance);

                    BigDecimal withdrawalAmount = new BigDecimal(sc.nextLine());

                    Transaction y = new Transaction(
                            tID++,
                            Transaction.TransactionType.WITHDRAWAL,
                            withdrawalAmount,
                            loggedInAccount.accID,
                            null
                            );
                    transactionList.add(y);

                    loggedInAccount.withdrawMoney(withdrawalAmount);

                    IO.println("Guncel bakiye: " + loggedInAccount.balance);
                    break;
                case 3:
                    IO.println("Para gondermek istediginiz hesabın ID sini girin..");
                    int targetAccount = sc.nextInt();
                    sc.nextLine();

                    IO.println("Gondermek istediğiniz para miktarını girin..");
                    BigDecimal amount = new BigDecimal(sc.nextLine());

                    if (loggedInAccount.balance.compareTo(amount) >= 0)
                    {
                        for (Account account1 : accountList)
                        {
                            if (account1.accID == targetAccount)
                            {
                                loggedInAccount.balance = loggedInAccount.balance.subtract(amount);

                                Transaction z = new Transaction(
                                        tID++,
                                        Transaction.TransactionType.SENT,
                                        amount,
                                        loggedInAccount.accID,
                                        targetAccount
                                );
                                transactionList.add(z);

                                account1.balance = account1.balance.add(amount);


                                IO.println(amount + " lira basari ile gonderildi..");
                            }
                        }

                    }else IO.println("yeterli para mevcut degildir..");
                    break;
                case 4:

                    writeLog(transactionList, loggedInAccount);

                    break;
                case 5:
                    IO.println("Hesaptan cikiliyor..");
                    return;
            }
        }
    }

    static void writeLog(List<Transaction> transactionList, Account loggedInAccount) {

        int num = 1;
        for (Transaction tr1 : transactionList)
        {
            if (tr1.sourceID == loggedInAccount.accID ||
                    (tr1.targetID != null && tr1.targetID == loggedInAccount.accID) // null fix
            )
            {
                IO.println("numara: " + num++ +
                        "\n logID: " + tr1.transactionID +
                        "\n islem turu: " + tr1.type +
                        "\n miktar: " + tr1.amount +
                        "\n gonderen ID: " + tr1.sourceID +
                        "\n teslim alan ID: " + tr1.targetID + "\t");
            }
        }
    }

}


