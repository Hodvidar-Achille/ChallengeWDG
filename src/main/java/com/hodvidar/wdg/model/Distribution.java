package com.hodvidar.wdg.model;

import com.hodvidar.wdg.dao.mock.DistributionDaoMock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

public class Distribution {

    private final int id;
    private final BigDecimal initialAmount;
    private final Date startDate;
    private final Date endDate;
    private final int companyId;
    private final int userId;
    private BigDecimal currentAmount;

    public Distribution(final int companyId,
                        final int userId,
                        final BigDecimal initialAmount) {
        this.id = DistributionDaoMock.getNewId();
        this.initialAmount = initialAmount.setScale(2, RoundingMode.CEILING);
        this.currentAmount = this.initialAmount;
        Calendar cal = Calendar.getInstance();
        this.startDate = cal.getTime();
        cal.add(Calendar.DATE, 365);
        this.endDate = cal.getTime();
        this.companyId = companyId;
        this.userId = userId;
    }

    Distribution(final int companyId,
                 final int userId,
                 final BigDecimal initialAmount,
                 final Date startDate) {
        this.id = DistributionDaoMock.getNewId();
        this.initialAmount = initialAmount.setScale(2, RoundingMode.CEILING);
        this.currentAmount = this.initialAmount;
        this.startDate = startDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.DATE, 365);
        this.endDate = cal.getTime();
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
}
