package com.example.greenprojectB.controller;

import com.example.greenprojectB.Handler.TimeHandler;
import com.example.greenprojectB.dto.SummarySensorDto;
import com.example.greenprojectB.entity.Company;
import com.example.greenprojectB.entity.Member;
import com.example.greenprojectB.entity.Sensor;
import com.example.greenprojectB.entity.Threshold;
import com.example.greenprojectB.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@lombok.extern.slf4j.Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/index")
    public String adminPageGet(HttpServletRequest request, Model model){
        //String sName = (String)request.getAttribute("sName");
        String sName = "1000003";

        Member member = adminService.getMemberId(sName);
        ArrayList<Member> members = adminService.getMembers();

        System.out.println("member ========================================================================> "+member);
        System.out.println("sName ========================================================================> "+sName);

        model.addAttribute("member", member);
        model.addAttribute("members", members);

        return "admin/index";
    }

    @ResponseBody
    @PostMapping("/deleteMember")
    public int deleteMemberPost(String memberId){
        System.out.println("deleteMemberId ========================================================================> "+memberId);
        int res = adminService.setDeleteMember(memberId);
        System.out.println("deleteres ========================================================================> "+res);

        return adminService.setDeleteMember(memberId);
    }


}
