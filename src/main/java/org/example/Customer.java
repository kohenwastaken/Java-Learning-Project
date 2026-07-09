package org.example;

public class Customer {

    private final int accID;
    private String name;
    private String surname;
    private String password;

    public Customer(String name, String surname,String password, int accID)
    {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.accID = accID;
    }

    public int getAccId() {
        return accID;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public boolean passwordMatches(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}
