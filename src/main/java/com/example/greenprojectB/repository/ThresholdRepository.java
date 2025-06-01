package com.example.greenprojectB.repository;

import com.example.greenprojectB.entity.Member;
import com.example.greenprojectB.entity.Threshold;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface ThresholdRepository extends JpaRepository<Threshold, Long> {
    ArrayList<Threshold> findByCompany_CompanyIdAndDeviceCode(String companyId, String deviceCode);
    ArrayList<Threshold> findByCompany_CompanyId(String companyId);
}
