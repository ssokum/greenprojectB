package com.example.greenprojectB.Handler;

import com.example.greenprojectB.controller.ProfessionalController;
import com.example.greenprojectB.dto.SummarySensorDto;
import com.example.greenprojectB.entity.Sensor;
import com.example.greenprojectB.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@RequiredArgsConstructor
@Component
public class TimeHandler {
    private int curTime = 0;

    @Autowired
    private ProfessionalController professionalController;

    @Scheduled(fixedDelay = 1000)
    public void run() {
        curTime += 1;

        professionalController.updateAllSensorData(curTime);
    }

    public int curTimeGet(){
        return curTime;
    }
}
