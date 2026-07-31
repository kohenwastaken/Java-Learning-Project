package org.example.bank.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class CustomerJpaEntity {


    @Id
    @Column(name = "account_id")
    private Integer accountId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String password;

    protected CustomerJpaEntity() {
    }

    public CustomerJpaEntity(
            Integer accountId,
            String name,
            String surname,
            String password
    ) {
        this.accountId = accountId;
        this.name = name;
        this.surname = surname;
        this.password = password;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPassword() {
        return password;
    }

}
