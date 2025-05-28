package com.example.greenprojectB.repository;

import com.example.greenprojectB.entity.Company;
import com.example.greenprojectB.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Long> {

}
