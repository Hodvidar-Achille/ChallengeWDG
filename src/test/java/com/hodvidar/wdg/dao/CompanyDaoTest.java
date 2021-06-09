package com.hodvidar.wdg.dao;

import com.hodvidar.wdg.dao.api.CompanyDao;
import com.hodvidar.wdg.dao.mock.CompanyDaoMock;
import com.hodvidar.wdg.model.Company;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CompanyDaoTest {

    CompanyDao companyDao;

    @BeforeEach
    void setUp() {
        companyDao = new CompanyDaoMock();
    }

    @Test
    void when_companyDao_saveCompany_getCompanyReturnsTheSameObject() {
        final int id1 = 1;
        final int id2 = 2;
        final Company c1 = new Company(1, "c1", BigDecimal.ZERO);
        final Company c2 = new Company(2, "c2", BigDecimal.ZERO);
        companyDao.saveCompany(c1);
        companyDao.saveCompany(c2);
        assertThat(companyDao.getCompany(id1)).isEqualToComparingFieldByField(c1);
        assertThat(companyDao.getCompany(id2)).isEqualToComparingFieldByField(c2);
    }

    @Test
    void when_companyDao_saveAlreadyExistingId_thenMeet_IllegalStateException() {
        final Company c1 = new Company(1, "c1", BigDecimal.ZERO);
        final Company c2 = new Company(1, "c2", BigDecimal.ZERO);
        companyDao.saveCompany(c1);
        final Throwable exception = assertThrows(IllegalStateException.class,
                () -> companyDao.saveCompany(c2));
        assertEquals("Company already exist for this id '1'", exception.getMessage());
    }

    @Test
    void when_companyDao_useWrongId_thenMeet_NotFoundException() {
        final Throwable exception = assertThrows(ResourceNotFoundException.class,
                () -> companyDao.getCompany(100));
        assertEquals("No company for id '100'", exception.getMessage());
    }

}
