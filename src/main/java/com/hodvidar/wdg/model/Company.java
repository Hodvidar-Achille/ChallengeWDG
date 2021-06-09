package com.hodvidar.wdg.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Company {

    private final int id;
    private final String name;
    private BigDecimal balance;

    public Company(final int id, final String name, final BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.balance = balance.setScale(2, RoundingMode.CEILING);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
