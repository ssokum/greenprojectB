package com.example.greenprojectB.controller;

import com.example.greenprojectB.dto.SummarySensorDto;
import com.example.greenprojectB.entity.Sensor;
import com.example.greenprojectB.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

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




}
