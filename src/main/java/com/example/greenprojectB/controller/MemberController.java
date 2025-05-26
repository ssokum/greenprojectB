package com.example.greenprojectB.controller;

import com.example.greenprojectB.dto.MemberDto;
import com.example.greenprojectB.entity.Member;
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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
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
  private final PasswordEncoder passwordEncoder;


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
    System.out.println("================> 로그인post통과(id) : " + id);
    Member member = memberService.getMemberDto(id);
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

}
