package com.example.greenprojectB.controller;

import com.example.greenprojectB.entity.Sensor;
import com.example.greenprojectB.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@lombok.extern.slf4j.Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    String[] sensorValue = {
            "ammonia",
            "benzene",
            "carbon",
            "co2",
            "floor_1",
            "floor_2",
            "floor_3",
            "hcho",
            "humidity",
            "illuminance",
            "indoor",
            "no2",
            "no_touch_temp",
            "noise",
            "ozone",
            "pm10",
            "pm1_0",
            "pm2_5",
            "so2",
            "voc"
    };

    private final AdminService adminService;

    @GetMapping("/index")
    public String adminIndexGet(Model model) {
        model.addAttribute("sensorValue", sensorValue);

        return "admin/index";
    }

    @ResponseBody
    @PostMapping("/updateSensorChart")
    public ArrayList<Sensor> updateSensorChartPost(int time) {
        ArrayList<Sensor> sensors = adminService.getSensors(time);

        System.out.println("sensors: " + sensors);

        return sensors;

    }




}
