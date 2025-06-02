package com.example.greenprojectB.controller;

import com.example.greenprojectB.entity.Equipment;
import com.example.greenprojectB.entity.Member;
import com.example.greenprojectB.service.EquipmentService;
import com.example.greenprojectB.service.MemberService;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/equipment")
public class EquipmentController {

  private final EquipmentService equipmentService;
  private final MemberService memberService;

  @GetMapping("/equipmentList")
  public String equipmentListGet(Model model,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 @PageableDefault(size = 9, sort = "equipmentId", direction = Sort.Direction.DESC) Pageable pageable) {

    Page<Equipment> equipmentPage = equipmentService.getEquipmentPage(pageable);
    model.addAttribute("equipmentList", equipmentPage.getContent());

    String[] img1 = new String[50];
    String[] img2 = new String[50];
    int i = 0;
    for(Equipment equipment : equipmentPage.getContent()) {
      System.out.println("equipmentPage.getContent() : " + equipment.getContent());
      int position = equipment.getContent().indexOf("src=\"/") + 21;
      String content = equipment.getContent().substring(position);
      img1[i] = content.substring(0, content.indexOf("\""));
      System.out.println("img1[] : " + img1[i]);

      position = content.indexOf("src=\"/") + 21;
      System.out.println("position : " + position);
      content = content.substring(position);
      System.out.println("content : " + content);
      img2[i] = content.substring(0, content.indexOf("\""));
      System.out.println("img2[] : " + img2[i]);

      i++;
    }
    model.addAttribute("img1", img1);
    model.addAttribute("img2", img2);

    if (userDetails != null) {
      Member member = memberService.findByMemberId(userDetails.getUsername());
      model.addAttribute("loginRole", member.getRole());
    }

    return "equipment/equipmentList";
  }

  @GetMapping("/equipmentInput")
  public String equipmentInputGet(Model model,
                                  @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) return "redirect:/member/memberLogin";

    Member member = memberService.findByMemberId(userDetails.getUsername());
    if (!"ADMIN".equalsIgnoreCase(String.valueOf(member.getRole()))) return "redirect:/message/accessDenied";

    model.addAttribute("equipment", new Equipment());
    return "equipment/equipmentInput";
  }

  @PostMapping("/equipmentInput")
  public String equipmentInputPost(Equipment equipment,
                                   @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) return "redirect:/member/memberLogin";

    Member member = memberService.findByMemberId(userDetails.getUsername());
    if (!"ADMIN".equalsIgnoreCase(String.valueOf(member.getRole()))) return "redirect:/message/accessDenied";

    equipment.setMember(member);
    Equipment equipmentVo = equipmentService.save(equipment);

    if (equipmentVo != null) return "redirect:/message/equipmentInputOk";
    else return "redirect:/message/equipmentInputNo";
  }

  @GetMapping("/equipmentDetail/{equipmentId}")
  public String equipmentDetailGet(@PathVariable Long equipmentId, Model model) {
    Equipment equipment = equipmentService.findById(equipmentId);
    if (equipment == null) return "redirect:/message/equipmentNotFound";

    model.addAttribute("equipment", equipment);
    return "equipment/equipmentDetail";
  }

  @GetMapping("/equipmentUpdate/{equipmentId}")
  public String equipmentUpdateGet(@PathVariable Long equipmentId, Model model,
                                 @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) return "redirect:/member/memberLogin";

    Equipment equipment = equipmentService.findById(equipmentId);
    if (equipment == null) return "redirect:/message/equipmentNotFound";

    model.addAttribute("equipment", equipment);
    return "equipment/equipmentUpdate";
  }

  @PostMapping("/equipmentUpdate/{equipmentId}")
  public String equipmentUpdatePost(@PathVariable Long equipmentId,
                                  @ModelAttribute Equipment equipment,
                                  @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) return "redirect:/member/memberLogin";

    Equipment origin = equipmentService.findById(equipmentId);
    if (origin == null) return "redirect:/message/equipmentNotFound";

    origin.setTitle(equipment.getTitle());
    origin.setContent(equipment.getContent());
    equipmentService.save(origin);

    return "redirect:/message/equipmentUpdateOk";
  }

  @GetMapping("/equipmentDelete/{equipmentId}")
  public String equipmentDeleteGet(@PathVariable Long equipmentId,
                                   @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) return "redirect:/member/memberLogin";

    Equipment equipment = equipmentService.findById(equipmentId);
    if (equipment != null) {
      equipmentService.delete(equipmentId);
      return "redirect:/message/equipmentDeleteOk";
    } else {
      return "redirect:/message/equipmentNotFound";
    }
  }
}
