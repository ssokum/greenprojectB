package com.example.greenprojectB.controller;

import com.example.greenprojectB.constant.Role;
import com.example.greenprojectB.dto.CompanyDto;
import com.example.greenprojectB.dto.HistoryDto;
import com.example.greenprojectB.dto.MemberDto;
import com.example.greenprojectB.dto.SummarySensorDto;
import com.example.greenprojectB.entity.Company;
import com.example.greenprojectB.entity.History;
import com.example.greenprojectB.entity.Member;
import com.example.greenprojectB.repository.HistoryRepository;
import com.example.greenprojectB.service.AdminService;
import com.example.greenprojectB.service.CompanyService;
import com.example.greenprojectB.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/company")
public class CompanyController {

  private final CompanyService companyService;
  private final HistoryRepository historyRepository;
  //private final CompanyRepository companyRepository;
  private final PasswordEncoder passwordEncoder;
  private final MemberService memberService;

  // 내용 - 회사소개(이하메뉴) + 참여기업(이하메뉴) + 기업회원가입기능


  // 회사소개 -
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
  public String companyJoinPost(CompanyDto dto) {
    // 주소 조합
    String address = dto.getPostCode() + "/" +
            dto.getAddress() + " "
            + (dto.getAddress２() != null ? dto.getAddress２() : "")
            + (dto.getAddress１() != null ? dto.getAddress１() : "");

    log.info("=============> dto처음 : " + dto);

    // 일반회원으로 먼저 가입 처리한다.
    MemberDto memberDto = new MemberDto();
    memberDto.setMemberId(dto.getCompanyId());
    memberDto.setMemberName(dto.getCompanyName());
    memberDto.setMemberEmail(dto.getCompanyEmail());
    memberDto.setPhoneNumber(dto.getPhoneNumber());
    memberDto.setRole(Role.COMPANY);
    memberDto.setPassword(dto.getPassword());

    log.info("=============> memberDto : " + memberDto);
    Member member = Member.createMember(memberDto, passwordEncoder);  // DTO객체를 Entity객체로 변화
    Member memberRes = memberService.saveMember(member);              // 회원 가입 처리
    log.info("=============> entity : " + memberRes);

    // 일반회원 가입완료후 다시 기업회원으로 가입처리한다.
    dto.setAddress(address);
    Company company = Company.createCompany(dto);
    companyService.setCompanyInput(company);
    return "company/companyLogin";
  }

  // 기업회원 로그인 폼 불러오기
  @GetMapping("/companyLogin")
  public String companyLoginGet() {
    return "company/companyLogin";
  }

  // 정상적인 로그인처리 완료시 이곳을 통과
  @PostMapping("/companyLogin")
  public String companyLoginPost(HttpServletRequest request, HttpServletResponse response,
                                 Authentication authentication,
                                 RedirectAttributes rttr,
                                 String companyId, String password) {
    // Spring Security에서 사용자 정보를 가져온다.
    // String id = authentication.getName().toString();

    // 1. company에서 조회
    System.out.println("================> 로그인post(id) : " + companyId);
    Company company = companyService.getCompanyDto(companyId);

    //회사 로그인 처리
    if(company != null) {
      System.out.println("================> 로그인post통과(dto) : " + company);
      rttr.addFlashAttribute("message", company.getCompanyName() + "님 로그인 되었습니다.");

      // 세션처리....
      HttpSession session = request.getSession();
      session.setAttribute("sCName", company.getCompanyName());

      // 쿠키처리....

      // 등급(레벨) 처리
//      String strLevel = company.getRole().toString();
//      if (strLevel.equals("ADMIN")) strLevel = "관리자";
//      else if (strLevel.equals("OPERATOR")) strLevel = "운영자";
//      else if (strLevel.equals("USER")) strLevel = "정회원";
//
//      log.info("====================>> 회원 등급 : " + strLevel + " , sName : " + company.getCompanyName());
//      session.setAttribute("strLevel", strLevel);

      return "redirect:/";
    }
    else {
      System.out.println("로그인 실패~");
      rttr.addFlashAttribute("message", "로그인 실패~ 다시 로그인해 주세요.");
      return "company/companyLogin";
    }
  }







  @GetMapping("/companyLogin/error")
  public String companyLoginErrorGet(RedirectAttributes rttr) {
    rttr.addFlashAttribute("cLoginErrorMsg", "기업아이디 또는 비밀번호가 일치하지 않습니다.");
    return "redirect:/company/companyLogin";
  }


  @GetMapping("/companyLoginOk")
  public String companyLoginOkPost(HttpServletRequest request, HttpServletResponse response,
                                 Authentication authentication,
                                 RedirectAttributes rttr) {
    // Spring Security에서 사용자 정보를 가져온다.
    String id = authentication.getName().toString();
    System.out.println("================> 로그인post통과(id) : " + id);
    Company company = companyService.getCompanyDto(id);
    System.out.println("================> 로그인post통과(dto) : " + company);
    rttr.addFlashAttribute("message", company.getCompanyName() + "님 로그인 되었습니다.");

    // 세션처리....
    HttpSession session = request.getSession();
    session.setAttribute("sName", company.getCompanyName());

    // 등급(레벨) 처리
    String strLevel = company.getRole().toString();
    System.out.print(strLevel);
    if(strLevel.equals("ADMIN")) strLevel = "관리자";
    else if(strLevel.equals("OPERATOR")) strLevel = "운영자";
    else if(strLevel.equals("USER")) strLevel = "정회원";

    log.info("====================>> 회원 등급 : " + strLevel + " , sName : " + company.getCompanyName());
    session.setAttribute("strLevel", strLevel);

    return "redirect:/";
  }


  // 기업 소개 -
  // 기업 상세보기
  @GetMapping("/company/companyDetail")
  public String companyDetailGet(@RequestParam("company_idx") Long company_idx, Model model ) {
    Optional<Company> company = companyService.findByCompanyId(company_idx);
    CompanyDto dto = CompanyDto.createCompanyDto(company);

    model.addAttribute("dto", dto);
    System.out.println("dto : " + dto);
    return "company/companyDetail";
  }







/*

  // 기업 소개 -
  // 레드라이즈(기업1) 상세보기
  @GetMapping("/enterprise/enterprise1")
  public String enterprise1Get(@RequestParam("company_idx") Long company_idx, Model model ) {
    Optional<Company> company = companyService.findByCompanyId(company_idx);
    CompanyDto dto = CompanyDto.createCompanyDto(company);

    model.addAttribute("dto", dto);
    return "enterprise/enterprise1";
  }

  // 화이트서버(기업2) 상세보기
  @GetMapping("/enterprise/enterprise2")
  public String enterprise2Get(@RequestParam("company_idx") Long company_idx, Model model ) {
    Optional<Company> company = companyService.findByCompanyId(company_idx);
    CompanyDto dto = CompanyDto.createCompanyDto(company);

    model.addAttribute("dto", dto);
    return "enterprise/enterprise2";
  }


  // 블루와이즈(기업3) 상세보기
  @GetMapping("/enterprise/enterprise3")
  public String enterprise3Get(@RequestParam("company_idx") Long company_idx, Model model ) {
    Optional<Company> company = companyService.findByCompanyId(company_idx);
    CompanyDto dto = CompanyDto.createCompanyDto(company);

    model.addAttribute("dto", dto);
    return "enterprise/enterprise3";
  }


  // (기업4) 상세보기
  @GetMapping("/enterprise/enterprise4")
  public String enterprise4Get(@RequestParam("company_idx") Long company_idx, Model model ) {
    Optional<Company> company = companyService.findByCompanyId(company_idx);
    CompanyDto dto = CompanyDto.createCompanyDto(company);

    model.addAttribute("dto", dto);
    return "enterprise/enterprise4";
  }


  // (기업5) 상세보기
  @GetMapping("/enterprise/enterprise5")
  public String enterprise5Get(@RequestParam("company_idx") Long company_idx, Model model ) {
    Optional<Company> company = companyService.findByCompanyId(company_idx);
    CompanyDto dto = CompanyDto.createCompanyDto(company);

    model.addAttribute("dto", dto);
    return "enterprise/enterprise5";
  }

  // (기업6) 상세보기
  @GetMapping("/enterprise/enterprise6")
  public String enterprise6Get(@RequestParam("company_idx") Long company_idx, Model model ) {
    Optional<Company> company = companyService.findByCompanyId(company_idx);
    CompanyDto dto = CompanyDto.createCompanyDto(company);

    model.addAttribute("dto", dto);
    return "enterprise/enterprise6";
  }


  // (기업7) 상세보기
  @GetMapping("/enterprise/enterprise7")
  public String enterprise7Get(@RequestParam("company_idx") Long company_idx, Model model ) {
    Optional<Company> company = companyService.findByCompanyId(company_idx);
    CompanyDto dto = CompanyDto.createCompanyDto(company);

    model.addAttribute("dto", dto);
    return "enterprise/enterprise7";
  }


  // (기업8) 상세보기
  @GetMapping("/enterprise/enterprise8")
  public String enterprise8Get(@RequestParam("company_idx") Long company_idx, Model model ) {
    Optional<Company> company = companyService.findByCompanyId(company_idx);
    CompanyDto dto = CompanyDto.createCompanyDto(company);

    model.addAttribute("dto", dto);
    return "enterprise/enterprise8";
  }
*/

}
