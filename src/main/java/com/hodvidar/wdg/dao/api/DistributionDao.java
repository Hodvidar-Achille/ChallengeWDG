package com.hodvidar.wdg.dao.api;

import com.hodvidar.wdg.model.Distribution;

public interface DistributionDao {

    Distribution getDistribution(int distributionId);

    void saveDistribution(Distribution distribution);
}
