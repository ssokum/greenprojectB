package com.example.greenprojectB.controller;

import com.example.greenprojectB.entity.Faq;
import com.example.greenprojectB.entity.Member;
import com.example.greenprojectB.service.FaqService;
import com.example.greenprojectB.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/faq")
public class FaqController {

  private final FaqService faqService;
  private final MemberService memberService;

  @GetMapping("/faqList")
  public String faqListGet(Model model,
                           @AuthenticationPrincipal UserDetails userDetails,
                           @PageableDefault(size = 10, sort = "faqIdx", direction = Sort.Direction.DESC) Pageable pageable) {

    Page<Faq> faqPage = faqService.getFaqPage(pageable);
    model.addAttribute("faqPage", faqPage);

    // 로그인한 사용자 정보 전달
    if (userDetails != null) {
      Member member = memberService.findByMemberId(userDetails.getUsername());
      model.addAttribute("loginRole", member.getRole());
    }

    return "faq/faqList";
  }

  // 글쓰기 폼 이동
  @GetMapping("/faqInput")
  public String faqInputGet(Model model, @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) return "redirect:/member/memberLogin"; // 로그인 안 되어 있으면 로그인 페이지로

    Member member = memberService.findByMemberId(userDetails.getUsername());

    model.addAttribute("faq", new Faq());
    return "faq/faqInput";
  }

  // 글쓰기 처리
  @PostMapping("/faqInput")
  public String faqInputPost(Faq faq,
                             @AuthenticationPrincipal UserDetails userDetails) {
    Member member = memberService.findByMemberId(userDetails.getUsername());
    faq.setMember(member);

    Faq faqVo = faqService.save(faq);

    if (faqVo != null) return "redirect:/message/faqInputOk";
    else return "redirect:/message/faqInputNo";
  }


  // FAQ 수정 폼
  @GetMapping("/faqUpdate")
  public String faqEditGet(@RequestParam Long faqIdx, Model model,
                           @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) return "redirect:/member/memberLogin";

    Faq faq = faqService.findById(faqIdx);
    if (faq == null) return "redirect:/message/faqNotFound";

    model.addAttribute("faq", faq);
    return "faq/faqUpdate";
  }

  // FAQ 수정 처리
  @PostMapping("/faqUpdate")
  public String faqEditPost(@ModelAttribute Faq faq,
                            @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) return "redirect:/member/memberLogin";

    Faq origin = faqService.findById(faq.getFaqIdx());
    if (origin == null) return "redirect:/message/faqNotFound";

    origin.setTitle(faq.getTitle());
    origin.setContent(faq.getContent());

    faqService.save(origin);

    return "redirect:/message/faqUpdateOk";
  }


  // FAQ 삭제
  @GetMapping("/faqDelete")
  public String faqDelete(@RequestParam Long faqIdx,
                          @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) return "redirect:/member/memberLogin";

    Faq faq = faqService.findById(faqIdx);
    if (faq != null) {
      faqService.delete(faqIdx);
      return "redirect:/message/faqDeleteOk";
    } else {
      return "redirect:/message/faqNotFound";
    }
  }
}
