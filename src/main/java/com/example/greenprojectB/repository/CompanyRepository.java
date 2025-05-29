package com.example.greenprojectB.repository;

import com.example.greenprojectB.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    Optional<Company> findByCompanyId(String companyId);
}
