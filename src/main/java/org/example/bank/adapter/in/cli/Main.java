package org.example.bank.adapter.in.cli;

import org.example.bank.adapter.out.memory.InMemoryAccountRepository;
import org.example.bank.adapter.out.memory.InMemoryCustomerRepository;
import org.example.bank.adapter.out.memory.InMemoryTransactionRepository;
import org.example.bank.application.port.out.AccountRepository;
import org.example.bank.application.port.out.CustomerRepository;
import org.example.bank.application.port.out.TransactionRepository;
import org.example.bank.application.service.BankService;
import org.example.bank.domain.model.Customer;
import org.example.bank.domain.model.Transaction;
import org.example.bank.domain.result.DepositResult;
import org.example.bank.domain.result.TransferResult;
import org.example.bank.domain.result.WithdrawResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        CustomerRepository customerRepository = new InMemoryCustomerRepository();

        AccountRepository accountRepository = new InMemoryAccountRepository();

        TransactionRepository transactionRepository = new InMemoryTransactionRepository();

        BankService bankService = new BankService(
                customerRepository,
                accountRepository,
                transactionRepository
        );

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

        return bankService.registerCustomer(firstName, lastName, pswrd).getAccId();
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

    static void accountMenu(Scanner sc, BankService bankService, Customer customer)
    {
        while (true)
        {
            int choice = readInt(sc, "Hosgeldiniz " + customer.getName() + " " + customer.getSurname() + " !" +
                    "\n Secenekleriniz asagidaki gibidir:" +
                    "\n 1. Para yatir" +
                    "\n 2. Para cek" +
                    "\n 3. Para gonder" +
                    "\n 4. Hesap hareketleri" +
                    "\n 5. Hesaptan cikis");

            switch (choice){
                case 1:

                    IO.println("mevcut bakiye: " + bankService.showBalance(customer.getAccId()));

                    BigDecimal depositAmount = readBigDecimal(sc, "yatirmak istediginiz miktari giriniz..");

                    DepositResult result = bankService.depositToAccount(customer.getAccId(), depositAmount);

                    switch (result){
                        case SUCCESS -> IO.println("Guncel bakiye: " + bankService.showBalance(customer.getAccId()));
                        case INVALID_AMOUNT -> IO.println("Yatirilan para 0 dan buyuk olmali..");
                    }

                    break;
                case 2:
                    IO.println("mevcut bakiye: " + bankService.showBalance(customer.getAccId()));

                    BigDecimal withdrawalAmount = readBigDecimal(sc, "cekmek istediginiz miktari giriniz..");

                    WithdrawResult result1 = bankService.withdrawFromAccount(customer.getAccId(), withdrawalAmount);

                    switch (result1){
                        case SUCCESS -> IO.println("Guncel bakiye: " + bankService.showBalance(customer.getAccId()));
                        case INSUFFICIENT_BALANCE -> IO.println("Yetersiz bakiye..");
                        case INVALID_AMOUNT -> IO.println("Gecersiz deger..");
                    }

                    break;
                case 3:

                    int targetID = readInt(sc, "Para gondermek istediginiz hesabın ID sini girin..");

                    BigDecimal amount = readBigDecimal(sc, "Gondermek istediginiz para miktarini girin..");

                    TransferResult result2 = bankService.transferFromAccount(customer.getAccId(), targetID, amount);

                    switch (result2){
                        case SUCCESS -> IO.println("Basari ile transfer edildi..");
                        case ACCOUNT_NOT_FOUND -> IO.println("Hesap bulunamadi..");
                        case INVALID_SELF_ID -> IO.println("Kendinize para gonderemezsiniz..");
                        case INVALID_AMOUNT -> IO.println("Gecersiz deger girisi..");
                        case INSUFFICIENT_BALANCE -> IO.println("Yetersiz bakiye..");
                    }

                    break;
                case 4:

                    List<Transaction> transactions = bankService.getTransactionsForAccount(customer.getAccId());
                    writeLog(transactions);

                    break;
                case 5:
                    IO.println("Hesaptan cikiliyor..");
                    return;
            }
        }
    }

    static void writeLog(List<Transaction> transactionList) {

        int num = 1;
        if (transactionList.isEmpty()) {
            IO.println("Henuz islem gecmisi yok.");
            return;
        }
        for (Transaction tr1 : transactionList)
        {
            IO.println("numara: " + num++ +
                    "\n logID: " + tr1.getTransactionID() +
                    "\n islem turu: " + tr1.getType() +
                    "\n miktar: " + tr1.getAmount() +
                    "\n gonderen ID: " + tr1.getSourceID() +
                    "\n teslim alan ID: " + tr1.getTargetID() + "\t");
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


