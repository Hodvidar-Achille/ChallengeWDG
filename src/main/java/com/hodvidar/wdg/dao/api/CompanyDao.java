package com.hodvidar.wdg.dao.api;

import com.hodvidar.wdg.model.Company;

public interface CompanyDao {

    Company getCompany(int companyId);

    void saveCompany(Company company);
}
