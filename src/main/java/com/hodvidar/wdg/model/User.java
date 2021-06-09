package com.hodvidar.wdg.model;

import com.hodvidar.wdg.exception.BalanceTooLowException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class User {

    private final int id;
    private final Map<Wallet, List<Distribution>> receivedDistributionsByWallet;
    private final Map<Wallet, BigDecimal> initialBalanceByWallet;

    /**
     * @deprecated use
     * {@link com.hodvidar.wdg.model.User#User(int)}}
     * instead and
     * {@link com.hodvidar.wdg.model.User#setInitialBalanceByWallet(int, BigDecimal)}
     */
    @Deprecated(since = "1.1", forRemoval = true)
    public User(final int id, final BigDecimal initialBalance) {
        this(id);
        this.initialBalanceByWallet.put(Wallet.GIFT, initialBalance.setScale(2, RoundingMode.CEILING));
    }

    public User(final int id) {
        this.id = id;
        this.initialBalanceByWallet = new EnumMap<>(Wallet.class);
        this.receivedDistributionsByWallet = new EnumMap<>(Wallet.class);
        Arrays.stream(Wallet.values()).forEach(this::addWallet);
    }

    private void addWallet(Wallet wallet) {
        this.initialBalanceByWallet.put(wallet, BigDecimal.ZERO);
        this.receivedDistributionsByWallet.put(wallet, new ArrayList<>());

    }

    public int getId() {
        return id;
    }

    public void setInitialBalanceByWallet(final int walletId,
                                          final BigDecimal initialBalance) {
        this.initialBalanceByWallet.put(Wallet.getWalletById(walletId), initialBalance.setScale(2, RoundingMode.CEILING));
    }

    /**
     * @deprecated use
     * {@link com.hodvidar.wdg.model.User#addDistribution(Wallet, Distribution)}
     * instead
     */
    @Deprecated(since = "1.1", forRemoval = true)
    public void addDistribution(final Distribution distribution) {
        addDistribution(Wallet.GIFT, distribution);
    }

    public void addDistribution(final Wallet wallet, final Distribution distribution) {
        this.receivedDistributionsByWallet.get(wallet).add(distribution);
    }

    /**
     * @deprecated use
     * {@link com.hodvidar.wdg.model.User#getBalance(Wallet)}
     * instead
     */
    @Deprecated(since = "1.1", forRemoval = true)
    public BigDecimal getBalance() {
        return getBalance(Wallet.GIFT);
    }

    public BigDecimal getBalance(Wallet wallet) {
        BigDecimal balance = this.initialBalanceByWallet.get(wallet);
        Date now = Calendar.getInstance().getTime();
        return balance.add(this.receivedDistributionsByWallet.get(wallet).stream()
                .filter(d -> d.getEndDate().after(now))
                .map(Distribution::getCurrentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    /**
     * Removes the amount from the user balance.
     *
     * @throws BalanceTooLowException if balance not sufficient
     * @deprecated use
     * {@link com.hodvidar.wdg.model.User#spendMoney(Wallet, BigDecimal)}
     * instead
     */
    @Deprecated(since = "1.1", forRemoval = true)
    public void spendMoney(final BigDecimal amount) throws BalanceTooLowException {
        spendMoney(Wallet.GIFT, amount);
    }

    public void spendMoney(final Wallet wallet, final BigDecimal amount) throws BalanceTooLowException {
        BigDecimal initialBalance = this.initialBalanceByWallet.get(wallet);
        if (this.getBalance(wallet).compareTo(amount) < 0) {
            throw new BalanceTooLowException("User '" + this.id
                    + "' cannot spend " + amount.toPlainString()
                    + " for wallet '" + wallet + "'");
        }
        if (initialBalance.compareTo(amount) >= 0) {
            this.initialBalanceByWallet.put(wallet, initialBalance.subtract(amount));
            return;
        }
        BigDecimal remainedAmountToSpend = amount.subtract(initialBalance);
        this.initialBalanceByWallet.put(wallet, BigDecimal.ZERO);
        Calendar now = Calendar.getInstance();
        for (Distribution distribution : this.receivedDistributionsByWallet.get(wallet)) {
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
}
