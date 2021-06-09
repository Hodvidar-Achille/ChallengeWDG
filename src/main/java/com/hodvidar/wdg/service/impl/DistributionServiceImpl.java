package com.hodvidar.wdg.service.impl;

import com.hodvidar.wdg.dao.api.CompanyDao;
import com.hodvidar.wdg.dao.api.DistributionDao;
import com.hodvidar.wdg.dao.api.UserDao;
import com.hodvidar.wdg.exception.BalanceTooLowException;
import com.hodvidar.wdg.model.Company;
import com.hodvidar.wdg.model.Distribution;
import com.hodvidar.wdg.model.User;
import com.hodvidar.wdg.service.api.DistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class DistributionServiceImpl implements DistributionService {

    private final CompanyDao companyDao;
    private final UserDao userDao;
    private final DistributionDao distributionDao;

    @Autowired
    public DistributionServiceImpl(final CompanyDao companyDao, final UserDao userDao, DistributionDao distributionDao) {
        this.companyDao = companyDao;
        this.userDao = userDao;
        this.distributionDao = distributionDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void distributeEndowment(final int companyId,
                                    final int userId,
                                    final BigDecimal amount)
            throws BalanceTooLowException {
        final Company company = this.companyDao.getCompany(companyId);
        final User user = this.userDao.getUser(userId);
        giveEndowmentFromCompanyToUser(company, user, amount);
    }

    private void giveEndowmentFromCompanyToUser(final Company company,
                                                final User user,
                                                final BigDecimal amount)
            throws BalanceTooLowException {
        if (company.getBalance().compareTo(amount) < 0) {
            throw new BalanceTooLowException("Cannot give endowment of '"
                    + amount.toPlainString() + "' to user '"
                    + user.getId() + "', company '"
                    + company.getName() + "' has a balance too low");
        }
        company.setBalance(company.getBalance().subtract(amount));
        Distribution currentDistribution = new Distribution(company.getId(),
                user.getId(),
                amount);
        distributionDao.saveDistribution(currentDistribution);
        user.addDistribution(currentDistribution);
    }
}
