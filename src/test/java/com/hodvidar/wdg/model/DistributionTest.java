package com.hodvidar.wdg.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class DistributionTest {

    @Test
    void when_createNewDistributionObject_endDateIsIn365Days() {
        final Distribution distribution = new Distribution(1, 1, BigDecimal.ZERO);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 365);
        Date nowPlus365Days = cal.getTime();
        assertThat(distribution.getEndDate()).isCloseTo(nowPlus365Days, 10000);
    }

    @Test
    void when_createNewDistributionObject_forWalletFood_endDateIsInFebruaryNextYear() {
        final Distribution distribution = new Distribution(1, 1, Wallet.FOOD.id, BigDecimal.ZERO);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        Date februaryNextYear = cal.getTime();
        assertThat(distribution.getEndDate()).isCloseTo(februaryNextYear, 10000);
    }
}
