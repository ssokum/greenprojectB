package com.example.greenprojectB.controller;

import com.example.greenprojectB.dto.CompanyDto;
import com.example.greenprojectB.dto.HistoryDto;
import com.example.greenprojectB.entity.Company;
import com.example.greenprojectB.entity.History;
import com.example.greenprojectB.repository.CompanyRepository;
import com.example.greenprojectB.repository.HistoryRepository;
import com.example.greenprojectB.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/company")
public class CompanyController {

  private final CompanyService companyService;
  private final HistoryRepository historyRepository;
  private final CompanyRepository companyRepository;
  private final PasswordEncoder passwordEncoder;


  // 인사말
  @GetMapping("/companyGreeting")
  public String companyGreetingGet() {
    return "company/companyGreeting";
  }

  // 일반현황 및 연혁
  @GetMapping("/companyHistory")
  public String companyHistoryGet(Model model) {
    LocalDateTime now = LocalDate.now().atStartOfDay();
    int thisYear = Year.now().getValue();
    int thisMonth = now.getMonthValue();
    int startYear = 1980;
    List<Integer> years = IntStream.rangeClosed(startYear, thisYear).boxed().collect(Collectors.toList());
    List<Integer> months = IntStream.rangeClosed(1, 12).boxed().toList();

    //연혁리스트 조회
    List<History> historyList = companyService.getHistoryList();

    //연도별로 그룹핑
    Map<Integer, List<History>> historyByYear = historyList.stream().collect(Collectors.groupingBy(h -> h.getHistoryDate().getYear(), Collectors.toList()));

    //연도 내림차순 정렬
    Map<Integer, List<History>> sortedHistoryByYear = new TreeMap<>(Comparator.reverseOrder());
    sortedHistoryByYear.putAll(historyByYear);

    model.addAttribute("thisYear", thisYear);
    model.addAttribute("thisMonth", thisMonth);
    model.addAttribute("years", years);
    model.addAttribute("months", months);
    model.addAttribute("historyByYear", sortedHistoryByYear);
    model.addAttribute("historyDto", new HistoryDto());
    return "company/companyHistory";
  }


  // 연혁 등록
  @PostMapping("/companyHistory")
  public String companyHistoryPost(HistoryDto dto, @RequestParam int year,@RequestParam int month) {
    // 연도와 월로 LocalDate 생성
    LocalDateTime historyDate = LocalDateTime.of(year, month, 1, 1, 1);
    // dto에 값 세팅
    dto.setHistoryDate(historyDate);
    // 엔티티 변환 및 저장
    History history = History.createHistory(dto);
    historyRepository.save(history);
    return "redirect:/company/companyHistory";
  }

  @GetMapping("/companyOrganizationChart")
  public String companyOrganizationChartGet() {
    return "company/companyOrganizationChart";
  }


  @GetMapping("/companyDirections")
  public String companyDirectionsGet() {
    return "company/companyDirections";
  }


  // 기업 회원가입 폼 부르기
  @GetMapping("/companyJoin")
  //public String companyJoinGet(Model model) {
  public String companyJoinGet() {
    //model.addAttribute("companyDto", new CompanyDto());
    return "company/companyJoin";
  }

  // 기업 회원가입 처리
  @PostMapping("/companyJoin")
  public String companyJoinPost(CompanyDto dto, MultipartFile sFile) {
    String oFileName =sFile.getOriginalFilename();
    System.out.println("oFileName ; " + oFileName);
    String filePath = null;
    if(sFile != null && !sFile.isEmpty()) {
      filePath = companyService.setFileUpload(sFile);
    }

    Company company = Company.createCompany(dto, passwordEncoder, filePath);
    companyRepository.save(company);
    return "member/memberLogin";
  }






}
