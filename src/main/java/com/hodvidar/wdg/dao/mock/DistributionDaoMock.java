package com.hodvidar.wdg.dao.mock;

import com.hodvidar.wdg.dao.api.DistributionDao;
import com.hodvidar.wdg.model.Distribution;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class DistributionDaoMock implements DistributionDao {

    private static int increment = 0;
    private final Map<Integer, Distribution> distributions;

    @Autowired
    public DistributionDaoMock() {
        this.distributions = new HashMap<>();
    }

    public static void setIncrementToZero() {
        increment = 0;
    }

    public static int getNewId() {
        increment += 1;
        return increment;
    }

    @Override
    public Distribution getDistribution(int distributionId) {
        Distribution distribution = this.distributions.get(distributionId);
        if (distribution == null) {
            throw new ResourceNotFoundException("No distribution for id '" + distributionId + "'");
        }
        return distribution;
    }

    @Override
    public void saveDistribution(Distribution distribution) {
        if (distributions.containsKey(distribution.getId())) {
            throw new IllegalStateException("Distribution already exist for this id '" + distribution.getId() + "'");
        }
        this.distributions.put(distribution.getId(), distribution);
    }
}
