package com.example.greenprojectB.controller;

import com.example.greenprojectB.entity.Faq;
import com.example.greenprojectB.entity.Member;
import com.example.greenprojectB.entity.Recruit;
import com.example.greenprojectB.service.MemberService;
import com.example.greenprojectB.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recruit")
public class RecruitController {

  private final RecruitService recruitService;
  private final MemberService memberService;

  @GetMapping("/recruitList")
  public String recruitList(Model model,
                            @AuthenticationPrincipal UserDetails userDetails,
                            @PageableDefault(size = 10, sort = "recruitIdx", direction = Sort.Direction.DESC) Pageable pageable) {
    Page<Recruit> recruitPage = recruitService.findAll(pageable); // 페이징 적용된 리스트
    model.addAttribute("recruitPage", recruitPage); // 템플릿에서 사용할 이름

    if (userDetails != null) {
      Member member = memberService.findByMemberId(userDetails.getUsername());
      model.addAttribute("loginRole", member.getRole().name()); // ENTERPRISE, USER, ADMIN 등
    }

    return "recruit/recruitList";
  }



  @GetMapping("/recruitInput")
  public String inputForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    Member member = memberService.findByMemberId(userDetails.getUsername());
    if (!member.getRole().name().equals("ENTERPRISE")) {
      return "redirect:/message/onlyEnterprise";
    }
    model.addAttribute("recruit", new Recruit());
    return "recruit/recruitInput";
  }

  @PostMapping("/recruitInput")
  public String faqInputPost(Recruit recruit,
                             @AuthenticationPrincipal UserDetails userDetails) {
    Member member = memberService.findByMemberId(userDetails.getUsername());
    recruit.setMember(member);

    Recruit recruitVo = recruitService.save(recruit);

    if (recruitVo != null) return "redirect:/message/recruitInputOk";
    else return "redirect:/message/recruitInputNo";
  }

  @GetMapping("/recruitDetail/{id}")
  public String detail(@PathVariable Long id,
                       @AuthenticationPrincipal UserDetails userDetails,
                       Model model) {
    Recruit recruit = recruitService.findById(id);
    if (recruit == null) return "redirect:/message/recruitNotFound";

    model.addAttribute("recruit", recruit);

    if (userDetails != null) {
      Member member = memberService.findByMemberId(userDetails.getUsername());
      model.addAttribute("loginId", member.getMemberId());
      model.addAttribute("loginRole", member.getRole().name()); // ADMIN, USER, ENTERPRISE
    }

    return "recruit/recruitDetail";
  }

  @PostMapping("/delete/{id}")
  public String delete(@PathVariable Long id,
                       @AuthenticationPrincipal UserDetails userDetails) {
    Recruit recruit = recruitService.findById(id);
    if (recruit == null) return "redirect:/message/recruitNotFound";

    Member member = memberService.findByMemberId(userDetails.getUsername());
    if (!member.getMemberId().equals(recruit.getMember().getMemberId()) && !member.getRole().name().equals("ADMIN")) {
      return "redirect:/message/noAuth";
    }

    recruitService.delete(id);
    return "redirect:/message/recruitDeleteOk";
  }
}
