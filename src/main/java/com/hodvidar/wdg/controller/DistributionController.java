package com.hodvidar.wdg.controller;

import com.hodvidar.wdg.exception.BalanceTooLowException;
import com.hodvidar.wdg.service.api.DistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/wdg/api/rest/company/{companyId}/user/{userId}")
public class DistributionController {

    private final DistributionService distributionService;

    @Autowired
    public DistributionController(DistributionService distributionService) {
        this.distributionService = distributionService;
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PostMapping(value = "/wallet/{wallerId}")
    public void addEndowment(@PathVariable final int companyId,
                             @PathVariable final int userId,
                             @PathVariable final int walletId,
                             @RequestBody final BigDecimal amount)
            throws BalanceTooLowException {
        this.distributionService.distributeEndowment(companyId, userId, walletId, amount);
    }
}
