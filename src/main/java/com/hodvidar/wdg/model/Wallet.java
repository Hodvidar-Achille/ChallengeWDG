package com.hodvidar.wdg.model;

import org.apache.velocity.exception.ResourceNotFoundException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public enum Wallet {

    GIFT(1, "gift cards", "GIFT"),
    FOOD(2, "food cards", "FOOD");

    public final int id;
    public final String name;
    public final String type;

    Wallet(final int id, final String name, final String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public static Wallet getWalletById(final int id) {
        return Arrays.stream(values())
                .filter(w -> w.id == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No wallet for id '" + id + "'"));
    }

    public Date getEndOfValidityDate(Calendar date) {
        return switch (this) {
            case GIFT -> add365Days(date);
            case FOOD -> toFebruaryNextYear(date);
        };
    }

    private Date add365Days(Calendar date) {
        date.add(Calendar.DATE, 365);
        return date.getTime();
    }

    private Date toFebruaryNextYear(Calendar date) {
        date.add(Calendar.YEAR, 1);
        date.set(Calendar.MONTH, 1);
        date.set(Calendar.DATE, date.getActualMaximum(Calendar.DATE));
        return date.getTime();
    }

}
