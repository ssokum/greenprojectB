package com.example.greenprojectB.controller;

import com.example.greenprojectB.Handler.TimeHandler;
import com.example.greenprojectB.dto.SummarySensorDto;
import com.example.greenprojectB.entity.Company;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@lombok.extern.slf4j.Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/index")
    public String adminIndexGet(Model model, HttpServletRequest request) {
        return "admin/index";
    }

    @GetMapping("/register-device")
    public String adminRegisterDeviceGet(Model model) {
        return "admin/register-device";
    }

    @GetMapping("/setup-device")
    public String adminSetupDeviceGet(Model model) {
        return "admin/setup-device";
    }

    @ResponseBody
    @PostMapping("/getCompany")
    public Company getCompanyPost(String companyId) {
        return adminService.getCompany(companyId);
    }

    @ResponseBody
    @PostMapping("/getDeviceCode")
    public List<String> getDeviceCodePost(String companyId) {
        return adminService.getDeviceCode(companyId);
    }

    @ResponseBody
    @PostMapping("/getThreshold")
    public ArrayList<Threshold> getThresholdPost(String companyId, String deviceCode) {
        return adminService.getThreshold(companyId, deviceCode);
    }

    @ResponseBody
    @PostMapping("/updateSensorByExcel")
    public String updateSensorByExcelPost(MultipartFile fName) {
        String oFileName = fName.getOriginalFilename();
        //System.out.println("==============>> oFileName : " + oFileName);
        adminService.fileCsvToMysql(fName);

        return "1";
    }



}
