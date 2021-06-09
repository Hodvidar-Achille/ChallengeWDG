package com.hodvidar.wdg.dao.mock;

import com.hodvidar.wdg.dao.api.CompanyDao;
import com.hodvidar.wdg.model.Company;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CompanyDaoMock implements CompanyDao {

    private final Map<Integer, Company> companies;

    @Autowired
    public CompanyDaoMock() {
        this.companies = new HashMap<>();
    }


    @Override
    public Company getCompany(int companyId) {
        Company company = this.companies.get(companyId);
        if (company == null) {
            throw new ResourceNotFoundException("No company for id '" + companyId + "'");
        }
        return company;
    }

    @Override
    public void saveCompany(Company company) {
        if (companies.containsKey(company.getId())) {
            throw new IllegalStateException("Company already exist for this id '" + company.getId() + "'");
        }
        this.companies.put(company.getId(), company);
    }
}
