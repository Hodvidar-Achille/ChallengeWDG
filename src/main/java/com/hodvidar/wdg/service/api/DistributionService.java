package com.hodvidar.wdg.service.api;

import com.hodvidar.wdg.exception.BalanceTooLowException;
import com.hodvidar.wdg.model.Wallet;

import java.math.BigDecimal;

public interface DistributionService {

    /**
     * @deprecated use
     * {@link com.hodvidar.wdg.service.api.DistributionService#distributeEndowment(int, int, int, BigDecimal)}
     * instead
     */
    @Deprecated(since = "1.1", forRemoval = true)
    default void distributeEndowment(int companyId, int userId, BigDecimal amount)
            throws BalanceTooLowException {
        distributeEndowment(companyId, userId, Wallet.GIFT.id, amount);
    }

    default void distributeGiftEndowment(int companyId, int userId, BigDecimal amount)
            throws BalanceTooLowException {
        distributeEndowment(companyId, userId, Wallet.GIFT.id, amount);
    }

    default void distributeMealVoucher(int companyId, int userId, BigDecimal amount)
            throws BalanceTooLowException {
        distributeEndowment(companyId, userId, Wallet.FOOD.id, amount);
    }

    void distributeEndowment(int companyId, int userId, int walletId, BigDecimal amount)
            throws BalanceTooLowException;
}
