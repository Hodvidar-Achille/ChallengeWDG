package com.hodvidar.wdg.model;

import com.hodvidar.wdg.exception.BalanceTooLowException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void when_userIsCreated_withBalance_itsBalanceIsTheSame() {
        final User u1 = new User(1, BigDecimal.valueOf(1500));
        assertThat(u1.getBalance()).isEqualByComparingTo("1500.00");
    }

    @Test
    void when_userGainNewDistribution_itsBalanceIsIncrease() {
        final User u1 = new User(1, BigDecimal.valueOf(1500));
        final Distribution d1 = new Distribution(1, 1, BigDecimal.valueOf(47));
        u1.addDistribution(d1);
        assertThat(u1.getBalance()).isEqualByComparingTo("1547.00");
    }

    @Test
    void when_userGainTooOldDistribution_itsBalanceStayTheSame() {
        final User u1 = new User(1, BigDecimal.valueOf(1500));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -10);
        Date oldDate = cal.getTime();
        final Distribution d1 = new Distribution(1, 1, BigDecimal.valueOf(47), oldDate);
        u1.addDistribution(d1);
        assertThat(u1.getBalance()).isEqualByComparingTo("1500.00");
    }

    @Test
    void when_spendsMoney_balanceIsLower() throws BalanceTooLowException {
        final User u1 = new User(1, BigDecimal.valueOf(1500));
        final Distribution d1 = new Distribution(1, 1, BigDecimal.valueOf(47));
        u1.addDistribution(d1);
        u1.spendMoney(BigDecimal.valueOf(1535.99));
        assertThat(u1.getBalance()).isEqualByComparingTo("11.01");
        assertThat(d1.getCurrentAmount()).isEqualByComparingTo("11.01");
        u1.spendMoney(BigDecimal.valueOf(5.50));
        assertThat(u1.getBalance()).isEqualByComparingTo("5.51");
        assertThat(d1.getCurrentAmount()).isEqualByComparingTo("5.51");
    }

}
