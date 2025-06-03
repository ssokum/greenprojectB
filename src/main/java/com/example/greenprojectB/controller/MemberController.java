package com.example.greenprojectB.controller;

import com.example.greenprojectB.constant.Role;
import com.example.greenprojectB.dto.MemberDto;
import com.example.greenprojectB.entity.Company;
import com.example.greenprojectB.entity.Member;
import com.example.greenprojectB.service.CompanyService;
import com.example.greenprojectB.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

  private final MemberService memberService;
  private final CompanyService companyService;
  private final PasswordEncoder passwordEncoder;

//  @Autowired
//  MemberService memberService;
//
//  @Autowired
//  PasswordEncoder passwordEncoder;


  @GetMapping("/memberJoin")
  public String memberJoinGet(Model model) {
    model.addAttribute("memberDto", new MemberDto());

    // 코드 추가
//    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
//    model.addAttribute("_csrf", csrfToken);

    return "member/memberJoin";
  }

  @PostMapping("/memberJoin")
  public String memberJoinPost(RedirectAttributes rttr,
                               @Valid MemberDto dto,
                               BindingResult bindingResult) {
    dto.setRole(Role.USER);
    log.info("=============> dto : " + dto);
    if(bindingResult.hasErrors()) {
      return "member/memberJoin";
    }

    // 유효성 검사 통과후 처리할내용(이메일 중복체크(서비스객체에서 수행처리), 회원 등록처리)
    try {
      Member member = Member.createMember(dto, passwordEncoder);  // DTO객체를 Entity객체로 변화
      Member memberRes = memberService.saveMember(member);        // 회원 가입 처리(중복처리도 수행하게 한다)
      log.info("=============> entity : " + memberRes);
      rttr.addFlashAttribute("message", "회원에 가입되셨습니다.");
      return "redirect:/member/memberLogin";
    } catch (IllegalStateException e) {
      System.out.println(e.getMessage());
      rttr.addFlashAttribute("message", "이미 가입된 회원입니다.(아이디 중복!)");
      return "redirect:/member/memberJoin";
    }
  }



  @GetMapping("/memberLogin")
  public String memberLoginGet() {
    return "member/memberLogin";
  }

  @GetMapping("/login/error")
  public String loginErrorGet(RedirectAttributes rttr) {
    rttr.addFlashAttribute("loginErrorMsg", "아이디 또는 비밀번호가 일치하지 않습니다.");
    return "redirect:/member/memberLogin";
  }

  // 정상적인 로그인처리 완료시 이곳을 통과
  @GetMapping("/memberLoginOk")
  public String memberLoginOkGet(HttpServletRequest request, HttpServletResponse response,
                                 Authentication authentication,
                                 RedirectAttributes rttr) {
    // Spring Security에서 사용자 정보를 가져온다.
    String id = authentication.getName().toString();

    // 1. 멤버에서 조회
    Member member = memberService.getMemberDto(id);
    System.out.println("================> 로그인post통과(id) : " + id + " , member.getRole() : " + member.getRole());

    if(member != null) {

      System.out.println("================> 로그인post통과(dto) : " + member);
      rttr.addFlashAttribute("message", member.getMemberName() + "님 로그인 되었습니다.");

      // 세션처리....
      HttpSession session = request.getSession();
      session.setAttribute("sName", member.getMemberName());

      // 쿠키처리....

      // 등급(레벨) 처리
      String strLevel = member.getRole().toString();
      if(strLevel.equals("ADMIN")) strLevel = "관리자";
      else if(strLevel.equals("OPERATOR")) strLevel = "운영자";
      else if(strLevel.equals("USER")) strLevel = "정회원";

      log.info("====================>> 회원 등급 : " + strLevel + " , sName : " + member.getMemberName());
      session.setAttribute("strLevel", strLevel);

      return "redirect:/";

    } else {
      //2. 멤버가 아니면 회사에서 조회
      Company company = companyService.getCompanyDto(id);
      if(company != null) {
        //회사 로그인 처리
        rttr.addFlashAttribute("message", company.getCompanyName() + "님 (기업) 로그인 되었습니다.");
        HttpSession session = request.getSession();
        session.setAttribute("sCName", company.getCompanyName());

        // 등급(레벨) 처리
//        String strLevel = company.getRole().toString();
//        if(strLevel.equals("ADMIN")) strLevel = "관리자";
//        else if(strLevel.equals("OPERATOR")) strLevel = "운영자";
//        else if(strLevel.equals("USER")) strLevel = "정회원";
//
//        log.info("====================>> 회원 등급 : " + strLevel + " , sName : " + company.getCompanyName());
//        session.setAttribute("strLevel", strLevel);


      }
      return "redirect:/";
    }
  }


  @GetMapping("/memberLogout")
  public String memberLogoutGet(HttpServletRequest request, HttpServletResponse response,
                                RedirectAttributes rttr) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if(authentication != null) {
      String id = authentication.getName().toString();
      log.info("=================>> email : " + id);
      Member member = memberService.getMemberDto(id);
      log.info("==============>> member : " + member);
      rttr.addFlashAttribute("message", member.getMemberName() + "님 로그아웃 되었습니다.");
      new SecurityContextLogoutHandler().logout(request, response, authentication);
      //session.invalidate();
    }
    return "redirect:/member/memberLogin";
  }

  @GetMapping("/memberResign")
  public String memberResignGet(HttpServletRequest request, HttpServletResponse response,
                                RedirectAttributes rttr) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if(authentication != null) {
      String id = authentication.getName().toString();
      String name = memberService.getMemberDto(id).getMemberName();
      memberService.setDeleteMember(id);

      rttr.addFlashAttribute("message", name + "님 회원탈퇴 되었습니다.");
      new SecurityContextLogoutHandler().logout(request, response, authentication);
      //session.invalidate();
    }

    return "redirect:/";
  }

  // 핸드폰 인증처리에 필요한 객체, 메소드
//  private final DefaultMessageService messageService;
//
//  public MemberController() {
//    this.messageService = NurigoApp.INSTANCE.initialize("NCSPHFIMIPN3R9II", "EKPGDNBOBEN3UNY4SRPTZPS1M1H7Q2PL", "https://api.coolsms.co.kr");
//  }

  /*// 6자리 인증번호 만들기
  public static String generateVerificationCode() {
    Random random = new Random();
    int code = 100000 + random.nextInt(900000);
    return String.format("%06d", code);
  }

  // 메세지 발송(단문)
  @ResponseBody
  @RequestMapping(value = "/accessPhoneNum", method = RequestMethod.POST)
  public SingleMessageSentResponse accessPhoneNumPost(@RequestParam String phoneNum, HttpSession session) {
    // 기존세션 제거
    session.removeAttribute("verificationCode_" + phoneNum);
    session.removeAttribute("verificationTime_" + phoneNum);

    String verificationCode = generateVerificationCode();

    Message message = new Message();


    message.setFrom("01093667008");
    message.setTo(phoneNum);
    message.setText("[GreenProject] 핸드폰 인증번호는 " + verificationCode + " 입니다.");

    SingleMessageSentResponse response = this.messageService.sendOne(new
            SingleMessageSendingRequest(message)); System.out.println(response);

    System.out.println("=================> 인증번호 : "+ verificationCode);

    // 새로운 인증번호와 시간을 세션에 저장
    session.setAttribute("verificationCode_" + phoneNum, verificationCode);
    session.setAttribute("verificationTime_" + phoneNum, System.currentTimeMillis());

    return response;
  }

  @ResponseBody
  @RequestMapping(value = "/verifyCode", method = RequestMethod.POST)
  public String verifyCodePost(@RequestParam String phoneNum, @RequestParam String inputCode, HttpSession session) {
    String storedCode = (String) session.getAttribute("verificationCode_" + phoneNum);
    Long storedTime = (Long) session.getAttribute("verificationTime_" + phoneNum);

    if (storedCode != null && storedTime != null) {
      long currentTime = System.currentTimeMillis();
      if (currentTime - storedTime <= 180000) { // 3분 이내
        if (storedCode.equals(inputCode)) {
          // 인증 성공
          session.removeAttribute("verificationCode_" + phoneNum);
          session.removeAttribute("verificationTime_" + phoneNum);
          return "1";
        }
      } else {
        // 시간 초과
        session.removeAttribute("verificationCode_" + phoneNum);
        session.removeAttribute("verificationTime_" + phoneNum);
      }
    }

    // 인증 실패
    return "0";
  }

  @ResponseBody
  @RequestMapping(value = "/expireVerification", method = RequestMethod.POST)
  public ResponseEntity<String> expireVerification(@RequestParam String phoneNum, HttpSession session) {
    session.removeAttribute("verificationCode_" + phoneNum);
    session.removeAttribute("verificationTime_" + phoneNum);
    return ResponseEntity.ok("Verification expired");
  }
*/

}

