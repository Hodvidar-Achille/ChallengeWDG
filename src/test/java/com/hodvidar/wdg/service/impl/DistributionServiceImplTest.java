package com.hodvidar.wdg.service.impl;

import com.hodvidar.wdg.dao.api.CompanyDao;
import com.hodvidar.wdg.dao.api.DistributionDao;
import com.hodvidar.wdg.dao.api.UserDao;
import com.hodvidar.wdg.dao.mock.CompanyDaoMock;
import com.hodvidar.wdg.dao.mock.DistributionDaoMock;
import com.hodvidar.wdg.dao.mock.UserDaoMock;
import com.hodvidar.wdg.exception.BalanceTooLowException;
import com.hodvidar.wdg.model.Company;
import com.hodvidar.wdg.model.Distribution;
import com.hodvidar.wdg.model.User;
import com.hodvidar.wdg.service.api.DistributionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DistributionServiceImplTest {

    CompanyDao companyDao;
    UserDao userDao;
    DistributionDao distributionDao;
    DistributionService service;

    @BeforeEach
    void setUp() {
        companyDao = new CompanyDaoMock();
        userDao = new UserDaoMock();
        distributionDao = new DistributionDaoMock();
        service = new DistributionServiceImpl(companyDao,
                userDao, distributionDao);
    }

    @Test
    void when_companyBalanceIsTooLow_distributeEndowment_meet_BalanceTooLowException() throws BalanceTooLowException {
        Company company1 = new Company(64, "c1", BigDecimal.valueOf(1000));
        companyDao.saveCompany(company1);
        User user1 = new User(69, BigDecimal.valueOf(100));
        userDao.saveUser(user1);
        final Throwable exception = assertThrows(BalanceTooLowException.class,
                () -> service.distributeEndowment(64, 69, BigDecimal.valueOf(5000)));
        assertEquals("Cannot give endowment of '5000' to user '69', company 'c1' has a balance too low",
                exception.getMessage());
        assertThat(company1.getBalance()).isEqualByComparingTo("1000.00");
        assertThat(user1.getBalance()).isEqualByComparingTo("100");
    }

    @Test
    void distributeEndowment_short() throws BalanceTooLowException {
        DistributionDaoMock.setIncrementToZero();

        Company company1 = new Company(1, "c1", BigDecimal.valueOf(1000));
        companyDao.saveCompany(company1);
        User user1 = new User(1, BigDecimal.valueOf(100));
        userDao.saveUser(user1);
        service.distributeEndowment(1, 1, BigDecimal.valueOf(50));
        assertThat(company1.getBalance()).isEqualByComparingTo("950.00");
        assertThat(user1.getBalance()).isEqualByComparingTo("150.00");
        Distribution d1 = distributionDao.getDistribution(1);
        Date now = Calendar.getInstance().getTime();
        assertThat(d1.getStartDate()).isCloseTo(now, 10000);
        assertThat(d1.getInitialAmount()).isEqualTo("50.00");
    }

    @Test
    void distributeEndowment_longerScenario() throws BalanceTooLowException {
        DistributionDaoMock.setIncrementToZero();

        Company company1 = new Company(1, "Wedoogift", BigDecimal.valueOf(1000));
        Company company2 = new Company(2, "Wedoofood", BigDecimal.valueOf(3000));
        companyDao.saveCompany(company1);
        companyDao.saveCompany(company2);
        User user1 = new User(1, BigDecimal.valueOf(100));
        User user2 = new User(2, BigDecimal.ZERO);
        User user3 = new User(3, BigDecimal.ZERO);
        userDao.saveUser(user1);
        userDao.saveUser(user2);
        userDao.saveUser(user3);

        service.distributeEndowment(1, 1, BigDecimal.valueOf(50));
        service.distributeEndowment(1, 2, BigDecimal.valueOf(100));
        service.distributeEndowment(2, 3, BigDecimal.valueOf(1000));

        assertThat(company1.getBalance()).isEqualByComparingTo("850.00");
        assertThat(company2.getBalance()).isEqualByComparingTo("2000.00");

        assertThat(user1.getBalance()).isEqualByComparingTo("150.00");
        assertThat(user2.getBalance()).isEqualByComparingTo("100.00");
        assertThat(user3.getBalance()).isEqualByComparingTo("1000.00");

        Distribution d1 = distributionDao.getDistribution(1);
        Distribution d2 = distributionDao.getDistribution(2);
        Distribution d3 = distributionDao.getDistribution(3);

        Date now = Calendar.getInstance().getTime();
        assertThat(d1.getStartDate()).isCloseTo(now, 10000);
        assertThat(d1.getInitialAmount()).isEqualTo("50.00");
        assertThat(d2.getInitialAmount()).isEqualTo("100.00");
        assertThat(d3.getInitialAmount()).isEqualTo("1000.00");

        assertThat(d1.getCompanyId()).isEqualTo(1);
        assertThat(d2.getCompanyId()).isEqualTo(1);
        assertThat(d3.getCompanyId()).isEqualTo(2);

        assertThat(d1.getUserId()).isEqualTo(1);
        assertThat(d2.getUserId()).isEqualTo(2);
        assertThat(d3.getUserId()).isEqualTo(3);
    }
}
