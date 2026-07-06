package org.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    //static int ID = 1;

    //static int tID = 1;

    public static void main() {

//        ArrayList<Customer> customerList = new ArrayList<Customer>();
//        ArrayList<Account> accountList = new ArrayList<Account>();
//        ArrayList<Transaction> transactionList = new ArrayList<Transaction>();

        BankService bankService = new BankService();

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

                    int userID = handleRegistry(sc, bankService);

                    IO.println("Hesap olusturuldu. ID'niz: " + userID + "\n");

                    break;
                case 2:

                    Customer customer = login(sc, bankService);

                    if (customer == null) break;

                    //giris sonrasi islemler
                    accountMenu(sc, bankService, customer);

                    break;
                case 3:
                    IO.println("uygulama kapatiliyor..");
                    return;
            }
        }
    }

    static int handleRegistry(Scanner sc, BankService bankService) {
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

        return bankService.registerCustomer(firstName, lastName, pswrd).accID;
    }

    static Customer login(Scanner sc, BankService bankService){

        int userID = readInt(sc, "Hesap ID giriniz..");

        IO.println("Sifre giriniz..");
        String enteredPswrd = sc.nextLine();

        Customer customer = bankService.loginAccount(userID, enteredPswrd);

        if (customer == null)
        {
            IO.println("ID ya da sifre yanlis. Sisteme geri donuluyor..");
            return null;
        }

        return customer;
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

    static void accountMenu(Scanner sc, BankService bankService, Customer customer)
    {
        Account account = findAffiliatedAccount(customer.accID, accountList);
        if (account == null) {
            IO.println("Beklenmeyen bir hata olustu..");
            return;
        }
        while (true)
        {
            int choice = readInt(sc, "Hosgeldiniz " + customer.name + " " + customer.surname + " !" +
                    "\n Secenekleriniz asagidaki gibidir:" +
                    "\n 1. Para yatir" +
                    "\n 2. Para cek" +
                    "\n 3. Para gonder" +
                    "\n 4. Hesap hareketleri" +
                    "\n 5. Hesaptan cikis");

            switch (choice){
                case 1:
                    IO.println("mevcut bakiye: " + account.balance);

                    BigDecimal depositAmount = readBigDecimal(sc, "yatirmak istediginiz miktari giriniz..");

                    if (!account.moneyDeposit(depositAmount)) {
                        IO.println("Yatirilan para 0 dan buyuk olmali..");
                        break;
                    }

                    Transaction x = new Transaction(
                            tID++,
                            Transaction.TransactionType.DEPOSIT,
                            depositAmount,
                            account.accID,
                            null
                    );
                    transactionList.add(x);

                    IO.println("Guncel bakiye: " + account.balance);
                    break;
                case 2:
                    IO.println("mevcut bakiye: " + account.balance);

                    BigDecimal withdrawalAmount = readBigDecimal(sc, "cekmek istediginiz miktari giriniz..");

                    if (!account.moneyWithdraw(withdrawalAmount)){
                        IO.println("lutfen gecerli bir deger giriniz..");
                        break;
                    }

                    Transaction y = new Transaction(
                            tID++,
                            Transaction.TransactionType.WITHDRAWAL,
                            withdrawalAmount,
                            account.accID,
                            null
                            );
                    transactionList.add(y);

                    IO.println("Guncel bakiye: " + account.balance);
                    break;
                case 3:

                    int targetID = readInt(sc, "Para gondermek istediginiz hesabın ID sini girin..");

                    if (targetID == account.accID) {
                        IO.println("Kendinize para gonderemezsiniz!");
                        break;
                    }

                    Account targetAccount = findAffiliatedAccount(targetID, accountList);
                    if (targetAccount == null) {
                        IO.println("Hesap bulunamadi..");
                        break;
                    }

                    BigDecimal amount = readBigDecimal(sc, "Gondermek istediginiz para miktarini girin..");

                    if (account.moneySend(amount)) {
                        targetAccount.moneyReceive(amount);

                        IO.println("Basari ile transfer edildi..");

                        Transaction z = new Transaction(
                                tID++,
                                Transaction.TransactionType.TRANSFER,
                                amount,
                                account.accID,
                                targetID
                        );
                        transactionList.add(z);
                    }else {
                        IO.println("gecersiz deger girisi..");
                    }

                    break;
                case 4:

                    writeLog(transactionList, account);

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


