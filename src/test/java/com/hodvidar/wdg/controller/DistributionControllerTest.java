package com.hodvidar.wdg.controller;

import com.hodvidar.wdg.dao.api.CompanyDao;
import com.hodvidar.wdg.dao.api.DistributionDao;
import com.hodvidar.wdg.dao.api.UserDao;
import com.hodvidar.wdg.dao.mock.CompanyDaoMock;
import com.hodvidar.wdg.dao.mock.DistributionDaoMock;
import com.hodvidar.wdg.dao.mock.UserDaoMock;
import com.hodvidar.wdg.model.Company;
import com.hodvidar.wdg.model.User;
import com.hodvidar.wdg.model.Wallet;
import com.hodvidar.wdg.service.api.DistributionService;
import com.hodvidar.wdg.service.impl.DistributionServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DistributionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    CompanyDao companyDao;
    UserDao userDao;
    DistributionDao distributionDao;
    DistributionService service;

    @Before
    public void init() {
        companyDao = new CompanyDaoMock();
        userDao = new UserDaoMock();
        distributionDao = new DistributionDaoMock();
        service = new DistributionServiceImpl(companyDao,
                userDao, distributionDao);
        Company company1 = new Company(1, "Wedoogift", BigDecimal.valueOf(1000));
        Company company2 = new Company(2, "Wedoofood", BigDecimal.valueOf(3000));
        companyDao.saveCompany(company1);
        companyDao.saveCompany(company2);
        User user1 = new User(1);
        user1.setInitialBalanceByWallet(Wallet.GIFT.id, BigDecimal.valueOf(100));
        User user2 = new User(2);
        User user3 = new User(3);
        userDao.saveUser(user1);
        userDao.saveUser(user2);
        userDao.saveUser(user3);
    }

    //@WithMockUser(username = "USER")
    @WithMockUser("USER")
    @Test
    public void find_login_ok() throws Exception {
        User user1 = userDao.getUser(1);
        assertThat(user1.getBalance(Wallet.GIFT)).isEqualByComparingTo("50.00");
        mockMvc.perform(post("/wdg/api/rest/company/1/user/1/wallet/1", BigDecimal.valueOf(100)))
                .andExpect(status().isNoContent());
        assertThat(user1.getBalance(Wallet.GIFT)).isEqualByComparingTo("150.00");
    }

    @Test
    public void find_nologin_401() throws Exception {
        mockMvc.perform(post("/wdg/api/rest/company/1/user/1/wallet/1", BigDecimal.valueOf(100)))
                .andExpect(status().isUnauthorized());
    }
}
