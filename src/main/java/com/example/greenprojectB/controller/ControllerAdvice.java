package com.example.greenprojectB.controller;

import com.example.greenprojectB.entity.Company;
import com.example.greenprojectB.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@org.springframework.web.bind.annotation.ControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

  private final CompanyService companyService;

  // 회사 목록을 보여주는 메서드
  @ModelAttribute("companies")
  public List<Company> companies() {
    return companyService.getAllCompanies();
  }

}
