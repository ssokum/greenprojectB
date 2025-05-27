package com.example.greenprojectB.service;

import com.example.greenprojectB.entity.History;
import com.example.greenprojectB.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyService {

  private final CompanyRepository companyRepository;

  public List<History> getHistoryList() {
    return companyRepository.findAll();
  }
}
