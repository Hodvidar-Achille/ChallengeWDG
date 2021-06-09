package com.hodvidar.wdg.model;

import com.hodvidar.wdg.exception.BalanceTooLowException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class User {

    private final int id;
    private final List<Distribution> receivedDistributions;
    private BigDecimal initialBalance;

    public User(final int id, final BigDecimal initialBalance) {
        this.id = id;
        this.initialBalance = initialBalance.setScale(2, RoundingMode.CEILING);
        this.receivedDistributions = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void addDistribution(final Distribution distribution) {
        this.receivedDistributions.add(distribution);
    }

    public BigDecimal getBalance() {
        BigDecimal balance = this.initialBalance;
        Date now = Calendar.getInstance().getTime();
        return balance.add(this.receivedDistributions.stream()
                .filter(d -> d.getEndDate().after(now))
                .map(Distribution::getCurrentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    /**
     * Removes the amount from the user balance.
     *
     * @throws BalanceTooLowException if balance not sufficient
     */
    public void spendMoney(final BigDecimal amount) throws BalanceTooLowException {
        if (this.getBalance().compareTo(amount) < 0) {
            throw new BalanceTooLowException("User '" + this.id
                    + "' cannot spend " + amount.toPlainString());
        }
        if (this.initialBalance.compareTo(amount) >= 0) {
            this.initialBalance = this.initialBalance.subtract(amount);
            return;
        }
        BigDecimal remainedAmountToSpend = amount.subtract(this.initialBalance);
        this.initialBalance = BigDecimal.ZERO;
        Calendar now = Calendar.getInstance();
        for (Distribution distribution : this.receivedDistributions) {
            if (!distribution.isValid(now)) {
                continue;
            }
            final BigDecimal amountReceived = distribution.getCurrentAmount();
            if (amountReceived.compareTo(remainedAmountToSpend) >= 0) {
                final BigDecimal remainingAmount = amountReceived.subtract(remainedAmountToSpend);
                distribution.setCurrentAmount(remainingAmount);
                return;
            }
            remainedAmountToSpend = remainedAmountToSpend.subtract(amountReceived);
            distribution.setCurrentAmount(BigDecimal.ZERO);
        }
    }

    // TODO method to clear old distributions from user list 'receivedDistributions'
}
