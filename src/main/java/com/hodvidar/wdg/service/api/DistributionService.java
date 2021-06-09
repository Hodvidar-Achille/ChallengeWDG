package com.hodvidar.wdg.service.api;

import com.hodvidar.wdg.exception.BalanceTooLowException;

import java.math.BigDecimal;

public interface DistributionService {

    void distributeEndowment(int companyId, int userId, BigDecimal amount)
            throws BalanceTooLowException;
}
