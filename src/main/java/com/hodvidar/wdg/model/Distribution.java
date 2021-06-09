package com.hodvidar.wdg.model;

import com.hodvidar.wdg.dao.mock.DistributionDaoMock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

public class Distribution {

    private final int id;
    private final Wallet wallet;
    private final BigDecimal initialAmount;
    private final Date startDate;
    private final Date endDate;
    private final int companyId;
    private final int userId;
    private BigDecimal currentAmount;

    /**
     * @deprecated use
     * {@link com.hodvidar.wdg.model.Distribution#Distribution(int, int, int, BigDecimal)}
     * instead
     */
    @Deprecated(since = "1.1", forRemoval = true)
    public Distribution(final int companyId,
                        final int userId,
                        final BigDecimal initialAmount) {
        this(companyId, userId, Wallet.GIFT.id, initialAmount);
    }

    public Distribution(final int companyId,
                        final int userId,
                        final int walletId,
                        final BigDecimal initialAmount) {
        this.id = DistributionDaoMock.getNewId();
        this.wallet = Wallet.getWalletById(walletId);
        this.initialAmount = initialAmount.setScale(2, RoundingMode.CEILING);
        this.currentAmount = this.initialAmount;
        Calendar cal = Calendar.getInstance();
        this.startDate = cal.getTime();
        this.endDate = wallet.getEndOfValidityDate(cal);
        this.companyId = companyId;
        this.userId = userId;
    }

    /**
     * @deprecated use
     * {@link com.hodvidar.wdg.model.Distribution#Distribution(int, int, int, BigDecimal, Date)}
     * instead
     */
    @Deprecated(since = "1.1", forRemoval = true)
    Distribution(final int companyId,
                 final int userId,
                 final BigDecimal initialAmount,
                 final Date startDate) {
        this(companyId, userId, Wallet.GIFT.id, initialAmount, startDate);
    }

    Distribution(final int companyId,
                 final int userId,
                 final int walletId,
                 final BigDecimal initialAmount,
                 final Date startDate) {
        this.id = DistributionDaoMock.getNewId();
        this.wallet = Wallet.getWalletById(walletId);
        this.initialAmount = initialAmount.setScale(2, RoundingMode.CEILING);
        this.currentAmount = this.initialAmount;
        this.startDate = startDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        this.endDate = this.wallet.getEndOfValidityDate(cal);
        this.companyId = companyId;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getInitialAmount() {
        return initialAmount;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getCompanyId() {
        return companyId;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isValid(Calendar now) {
        return now.getTime().before(this.endDate);
    }

    public int getWalletId() {
        return wallet.id;
    }
}
