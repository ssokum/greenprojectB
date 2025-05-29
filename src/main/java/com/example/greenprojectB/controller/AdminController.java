package com.example.greenprojectB.controller;

import com.example.greenprojectB.dto.SummarySensorDto;
import com.example.greenprojectB.entity.Company;
import com.example.greenprojectB.entity.Sensor;
import com.example.greenprojectB.entity.Threshold;
import com.example.greenprojectB.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@lombok.extern.slf4j.Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/index")
    public String adminIndexGet(Model model) {
        ArrayList<SummarySensorDto> summarySensors = adminService.getSummarySensors();

        model.addAttribute("summarySensors", summarySensors);

        System.out.println(summarySensors);

        return "admin/index";
    }

    @ResponseBody
    @PostMapping("/updateSensorData")
    public ArrayList<Sensor> updateSensorDataPost(int time) {
        ArrayList<Sensor> sensors = adminService.getChartSensors(time);

        System.out.println("sensors: " + sensors);

        return sensors;
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
        ArrayList<Threshold> thresholds = adminService.getThreshold(companyId, deviceCode);
        ArrayList<SummarySensorDto> summarySensors = adminService.getSummarySensors();
        for (Threshold threshold : thresholds) {
            for (SummarySensorDto summarySensor : summarySensors) {
                if (threshold.getSensorName().equals(summarySensor.getSensorName())) {
                    threshold.setSummarySensor(summarySensor);
                    break;
                }
            }
        }

        return adminService.getThreshold(companyId, deviceCode);
    }

}
