package com.example.greenprojectB.service;

import com.example.greenprojectB.dto.SummarySensorDto;
import com.example.greenprojectB.entity.Sensor;
import com.example.greenprojectB.repository.EventLogRepository;
import com.example.greenprojectB.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AdminService {
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

    private final SensorRepository sensorRepository;
    private final EventLogRepository eventLogRepository;

    public ArrayList<Sensor> getChartSensors(int minute){
        LocalDateTime baseTime = sensorRepository.findBySensorId(3935953).getMeasureDatetime().plusMinutes(minute);
        LocalDateTime targetTime = baseTime.plusMinutes(5);

        return sensorRepository.findByMeasureDatetimeBetween(baseTime, targetTime);
    }

    public ArrayList<SummarySensorDto> getSummarySensors(){
        ArrayList<SummarySensorDto> list = new ArrayList<>();

        for(int i = 0;  i< sensorValue.length; i++) {
            SummarySensorDto summarySensorDto = new SummarySensorDto();
            summarySensorDto.setSensorName(sensorValue[i]);
            summarySensorDto.setMin(0);
            summarySensorDto.setMean(0);
            summarySensorDto.setMax(0);
            summarySensorDto.setEventCnt(eventLogRepository.countByEventType(sensorValue[i]));

            list.add(summarySensorDto);
        }

        return list;
    }
}
