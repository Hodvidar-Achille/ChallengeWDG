package com.hodvidar.wdg.dao;

import com.hodvidar.wdg.dao.api.DistributionDao;
import com.hodvidar.wdg.dao.mock.DistributionDaoMock;
import com.hodvidar.wdg.model.Distribution;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DistributionDaoTest {

    DistributionDao distributionDao;

    @BeforeEach
    void setUp() {
        DistributionDaoMock.setIncrementToZero();
        distributionDao = new DistributionDaoMock();
    }

    @Test
    void when_distributionDao_saveDistribution_getDistributionReturnsTheSameObject() {
        final int id1 = 1;
        final int id2 = 2;
        final Distribution d1 = new Distribution(1, 1, BigDecimal.ZERO);
        final Distribution d2 = new Distribution(2, 2, BigDecimal.ZERO);
        distributionDao.saveDistribution(d1);
        distributionDao.saveDistribution(d2);
        assertThat(distributionDao.getDistribution(id1)).isEqualToComparingFieldByField(d1);
        assertThat(distributionDao.getDistribution(id2)).isEqualToComparingFieldByField(d2);
    }

    @Test
    void when_distributionDao_saveAlreadyExistingId_thenMeet_IllegalStateException() {
        final Distribution d1 = new Distribution(1, 1, BigDecimal.ZERO);
        distributionDao.saveDistribution(d1);
        final Throwable exception = assertThrows(IllegalStateException.class,
                () -> distributionDao.saveDistribution(d1));
        assertEquals("Distribution already exist for this id '1'", exception.getMessage());
    }

    @Test
    void when_distributionDao_useWrongId_thenMeet_NotFoundException() {
        final Throwable exception = assertThrows(ResourceNotFoundException.class,
                () -> distributionDao.getDistribution(100));
        assertEquals("No distribution for id '100'", exception.getMessage());
    }
}
