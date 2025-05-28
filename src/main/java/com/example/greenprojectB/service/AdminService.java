package com.example.greenprojectB.service;

import com.example.greenprojectB.entity.Sensor;
import com.example.greenprojectB.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final SensorRepository sensorRepository;

    public ArrayList<Sensor> getSensors(int minute){
        LocalDateTime baseTime = sensorRepository.findBySensorId(3935953).getMeasureDatetime().plusMinutes(minute);
        LocalDateTime targetTime = baseTime.plusMinutes(5);

        return sensorRepository.findByMeasureDatetimeBetween(baseTime, targetTime);
    }
}
