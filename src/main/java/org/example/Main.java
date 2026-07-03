package org.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static int ID = 1;

    static int tID = 1;

    public static void main() {

        ArrayList<Customer> customerList = new ArrayList<Customer>();
        ArrayList<Account> accountList = new ArrayList<Account>();
        ArrayList<Transaction> transactionList = new ArrayList<Transaction>();

        int choice;

        Scanner sc = new Scanner(System.in);

        while (true){

            choice = readInt(sc, "Banka sistemine hosgeldiniz." +
                    "\n Secenekleriniz asagidaki gibidir:" +
                    "\n 1. Kayit Ol" +
                    "\n 2. Giris yap" +
                    "\n 3. Cikis yap");

            switch (choice){
                case 1:
                    Customer registeredCustomer = registry(sc, ID++);
                    customerList.add(registeredCustomer);

                    Account registeredAccount = new Account(registeredCustomer.accID, new BigDecimal("1000"));
                    accountList.add(registeredAccount);

                    IO.println("Hesap olusturuldu. ID'niz: " + registeredAccount.accID + "\n");
                    break;
                case 2:
                    Customer loggedInCustomer = login(sc, customerList);
                    if (loggedInCustomer == null) break;

                    //giris sonrasi islemler
                    accountMenu(sc, loggedInCustomer, accountList, transactionList);

                    break;
                case 3:
                    IO.println("uygulama kapatiliyor..");
                    return;
            }
        }
    }

    static Customer registry(Scanner sc, int accID) {
        boolean conditions = false;
        String name = null;
        while (!conditions) {
            IO.println("Ad Soyad giriniz..");
            name = sc.nextLine();
            name = name.trim();
            if (name.contains(" ")) conditions = true;
        }

        IO.println("Sifre Olusturunuz..");
        String pswrd = sc.nextLine();

        int index = name.lastIndexOf(" ");

        String firstName = name.substring(0, index);
        String lastName = name.substring(index + 1);

        return new Customer(firstName, lastName, pswrd, accID);
    }

    static Customer login(Scanner sc, List<Customer> customerList){

        int accID = readInt(sc, "Hesap ID giriniz..");

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

    static Account findAffiliatedAccount(int accID, List<Account> accountList)
    {
        Account requestedAccount = null;
        for (Account account1 : accountList)
        {
            if(account1.accID == accID)
            {
                requestedAccount = account1;
                return requestedAccount;
            }
        }
        return requestedAccount;
    }

    static void accountMenu(Scanner sc, Customer loggedInCustomer, List<Account> accountList, List<Transaction> transactionList)
    {
        Account loggedInAccount = findAffiliatedAccount(loggedInCustomer.accID, accountList);
        if (loggedInAccount == null) {
            IO.println("Beklenmeyen bir hata olustu..");
            return;
        }
        while (true)
        {
            int choice = readInt(sc, "Hosgeldiniz " + loggedInCustomer.name + " " + loggedInCustomer.surname + " !" +
                    "\n Secenekleriniz asagidaki gibidir:" +
                    "\n 1. Para yatir" +
                    "\n 2. Para cek" +
                    "\n 3. Para gonder" +
                    "\n 4. Hesap hareketleri" +
                    "\n 5. Hesaptan cikis");

            switch (choice){
                case 1:
                    IO.println("mevcut bakiye: " + loggedInAccount.balance);

                    BigDecimal depositAmount = readBigDecimal(sc, "yatirmak istediginiz miktari giriniz..");

                    if (!loggedInAccount.moneyDeposit(depositAmount)) {
                        IO.println("Yatirilan para 0 dan buyuk olmali..");
                        break;
                    }

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
                    IO.println("mevcut bakiye: " + loggedInAccount.balance);

                    BigDecimal withdrawalAmount = readBigDecimal(sc, "cekmek istediginiz miktari giriniz..");

                    if (!loggedInAccount.moneyWithdraw(withdrawalAmount)){
                        IO.println("lutfen gecerli bir deger giriniz..");
                        break;
                    }

                    Transaction y = new Transaction(
                            tID++,
                            Transaction.TransactionType.WITHDRAWAL,
                            withdrawalAmount,
                            loggedInAccount.accID,
                            null
                            );
                    transactionList.add(y);

                    IO.println("Guncel bakiye: " + loggedInAccount.balance);
                    break;
                case 3:

                    int targetID = readInt(sc, "Para gondermek istediginiz hesabın ID sini girin..");

                    if (targetID == loggedInAccount.accID) {
                        IO.println("Kendinize para gonderemezsiniz!");
                        break;
                    }

                    Account targetAccount = findAffiliatedAccount(targetID, accountList);
                    if (targetAccount == null) {
                        IO.println("Hesap bulunamadi..");
                        break;
                    }

                    BigDecimal amount = readBigDecimal(sc, "Gondermek istediginiz para miktarini girin..");

                    if (loggedInAccount.moneySend(amount)) {
                        targetAccount.moneyReceive(amount);

                        IO.println("Basari ile transfer edildi..");

                        Transaction z = new Transaction(
                                tID++,
                                Transaction.TransactionType.TRANSFER,
                                amount,
                                loggedInAccount.accID,
                                targetID
                        );
                        transactionList.add(z);
                    }else {
                        IO.println("gecersiz deger girisi..");
                    }

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

    static int readInt (Scanner sc, String message) {

        while (true) {
            IO.println(message);

            String input = sc.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                IO.println("Lutfen gecerli bir tam sayi giriniz..");
            }
        }
    }

    static BigDecimal readBigDecimal (Scanner sc, String message) {

        while (true) {
            IO.println(message);

            String input = sc.nextLine().trim();
            input = input.replace(",", ".");

            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                IO.println("Lutfen gecerli bir para miktari giriniz.");
            }
        }
    }

}


